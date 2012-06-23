/*
Copyright 2012 Andrey Zaytsev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package net.meiolania.apps.habrahabr.fragments.posts;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.PostsSearchActivity;
import net.meiolania.apps.habrahabr.activities.PostsShowActivity;
import net.meiolania.apps.habrahabr.adapters.PostsAdapter;
import net.meiolania.apps.habrahabr.data.PostsData;
import net.meiolania.apps.habrahabr.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public abstract class AbstractionPostsFragment extends SherlockListFragment implements OnScrollListener{
    public final static String LOG_TAG = "PostsFragment";
    protected final ArrayList<PostsData> postsDatas = new ArrayList<PostsData>();
    protected PostsAdapter postsAdapter;
    protected int page = 0;
    protected boolean loadMoreData = true;
    protected boolean noMorePages = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        postsAdapter = new PostsAdapter(getActivity(), postsDatas);
        setListAdapter(postsAdapter);
        getListView().setOnScrollListener(this);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.posts_fragment, menu);
        
        final EditText searchQuery = (EditText) menu.findItem(R.id.search).getActionView().findViewById(R.id.search_query);
        searchQuery.setOnEditorActionListener(new OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    Intent intent = new Intent(getSherlockActivity(), PostsSearchActivity.class);
                    intent.putExtra(PostsSearchActivity.EXTRA_QUERY, searchQuery.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void loadList(){
        ++page;
        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
        new LoadPosts().execute();
    }

    protected abstract String getUrl();

    protected final class LoadPosts extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                Log.d(LOG_TAG, "Loading " + getUrl().replace("%page%", String.valueOf(page)));

                Document document = Jsoup.connect(getUrl().replace("%page%", String.valueOf(page))).get();
                Elements posts = document.select("div.post");
                
                if(posts.size() <= 0){
                    noMorePages = true;
                    /*
                     * It's a solve for:
                     * java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
                     */
                    getSherlockActivity().runOnUiThread(new Runnable(){
                        public void run(){
                            Toast.makeText(getSherlockActivity(), R.string.no_more_pages, Toast.LENGTH_SHORT).show();
                        }
                    });
                }    
                
                for(Element post : posts){
                    PostsData postsData = new PostsData();

                    final Element postTitle = post.select("a.post_title").first();
                    final Element hubs = post.select("div.hubs").first();
                    final Element date = post.select("div.published").first();
                    final Element author = post.select("div.author > a").first();
                    final Element comments = post.select("div.comments > span.all").first();

                    postsData.setTitle(postTitle.text());
                    postsData.setUrl(postTitle.attr("abs:href"));
                    postsData.setHubs(hubs.text());
                    postsData.setDate(date.text());
                    postsData.setAuthor(author != null ? author.text() : "");
                    postsData.setComments(comments != null ? Integer.valueOf(comments.text()) : 0);

                    postsDatas.add(postsData);
                }
            }
            catch(IOException e){
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            /*
             * Okay, that works. But I'm not sure that's a good solution.
             */
            getSherlockActivity().runOnUiThread(new Runnable(){
                public void run(){
                    if(!isCancelled())
                        postsAdapter.notifyDataSetChanged();
                    loadMoreData = true;
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                }
            });
        }

    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        showPost(position);
    }

    protected void showPost(int position){
        PostsData postsData = postsDatas.get(position);
        Intent intent = new Intent(getSherlockActivity(), PostsShowActivity.class);
        intent.putExtra(PostsShowActivity.EXTRA_URL, postsData.getUrl());
        intent.putExtra(PostsShowActivity.EXTRA_TITLE, postsData.getTitle());
        startActivity(intent);
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if((firstVisibleItem + visibleItemCount) == totalItemCount && loadMoreData && !noMorePages){
            loadMoreData = false;
            loadList();
            Log.i(LOG_TAG, "Loading " + page + " page");
            //TODO: need to find a better way to display a notification for devices with Android < 3.0
            if(!UIUtils.isHoneycombOrHigher())
                Toast.makeText(getSherlockActivity(), getString(R.string.loading_page, page), Toast.LENGTH_SHORT).show();
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState){

    }

}