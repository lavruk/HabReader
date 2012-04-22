package net.meiolania.apps.habrahabr.fragments;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.activities.PostsShowActivity;
import net.meiolania.apps.habrahabr.adapters.PostsAdapter;
import net.meiolania.apps.habrahabr.data.PostsData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public abstract class AbstractionPostsFragment extends SherlockListFragment implements OnScrollListener{
    public final static String LOG_TAG = "PostsFragment";
    protected final ArrayList<PostsData> postsDatas = new ArrayList<PostsData>();
    protected PostsAdapter postsAdapter;
    protected int page = 0;
    protected boolean loadMoreData = true;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        postsAdapter = new PostsAdapter(getActivity(), postsDatas);
        setListAdapter(postsAdapter);
        getListView().setOnScrollListener(this);
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
                Log.d(LOG_TAG, "Loading " + String.format(getUrl(), page));

                Document document = Jsoup.connect(String.format(getUrl(), page)).get();
                Elements posts = document.select("div.post");
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
            // getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
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
        final Intent intent = new Intent(getSherlockActivity(), PostsShowActivity.class);
        intent.putExtra(PostsShowActivity.EXTRA_URL, postsData.getUrl());
        startActivity(intent);
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if((firstVisibleItem + visibleItemCount) == totalItemCount && loadMoreData){
            loadMoreData = false;
            loadList();
            Log.i(LOG_TAG, "Loading " + page + " page");
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState){

    }

}