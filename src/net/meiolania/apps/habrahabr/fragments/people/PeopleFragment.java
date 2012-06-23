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

package net.meiolania.apps.habrahabr.fragments.people;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.PeopleSearchActivity;
import net.meiolania.apps.habrahabr.activities.PeopleShowActivity;
import net.meiolania.apps.habrahabr.adapters.PeopleAdapter;
import net.meiolania.apps.habrahabr.data.PeopleData;
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

public class PeopleFragment extends SherlockListFragment implements OnScrollListener{
    public final static String LOG_TAG = "PeopleFragment";
    public final static String DEFAULT_URL = "http://habrahabr.ru/people/page%page%/";
    protected final ArrayList<PeopleData> peopleDatas = new ArrayList<PeopleData>();
    protected boolean loadMoreData = true;
    protected PeopleAdapter peopleAdapter;
    protected int page = 0;
    protected String url;
    protected boolean noMorePages = false;
    
    public PeopleFragment(){
        url = DEFAULT_URL;
    }
    
    public PeopleFragment(String url){
        this.url = url;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        peopleAdapter = new PeopleAdapter(getSherlockActivity(), peopleDatas);
        setListAdapter(peopleAdapter);
        getListView().setOnScrollListener(this);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.people_fragment, menu);
        
        final EditText searchQuery = (EditText) menu.findItem(R.id.search).getActionView().findViewById(R.id.search_query);
        searchQuery.setOnEditorActionListener(new OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    Intent intent = new Intent(getSherlockActivity(), PeopleSearchActivity.class);
                    intent.putExtra(PeopleSearchActivity.EXTRA_QUERY, searchQuery.getText().toString());
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
        new LoadPeople().execute();
    }

    protected final class LoadPeople extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                Log.i(LOG_TAG, "Loading " + url.replace("%page%", String.valueOf(page)));

                Document document = Jsoup.connect(url.replace("%page%", String.valueOf(page))).get();
                Elements users = document.select("div.user");
                
                if(users.size() <= 0){
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
                
                for(Element user : users){
                    PeopleData peopleData = new PeopleData();
                    
                    Element rating = user.select("div.rating").first();
                    Element karma = user.select("div.karma").first();
                    Element avatar = user.select("div.avatar > a > img").first();
                    Element name = user.select("div.info > div.username > a").first();
                    Element lifetime = user.select("div.info > div.lifetime").first();
                    
                    peopleData.setName(name.text());
                    peopleData.setUrl(name.attr("abs:href"));
                    peopleData.setRating(rating.text());
                    peopleData.setKarma(karma.text());
                    peopleData.setAvatar(avatar.attr("src"));
                    peopleData.setLifetime(lifetime.text());
                    
                    peopleDatas.add(peopleData);
                }
            }
            catch(IOException e){
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            getSherlockActivity().runOnUiThread(new Runnable(){
                public void run(){
                    if(!isCancelled())
                        peopleAdapter.notifyDataSetChanged();
                    loadMoreData = true;
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                }
            });
        }

    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        showUser(position);
    }

    protected void showUser(int position){
        PeopleData peopleData = peopleDatas.get(position);
        Intent intent = new Intent(getSherlockActivity(), PeopleShowActivity.class);
        intent.putExtra(PeopleShowActivity.EXTRA_NAME, peopleData.getName());
        intent.putExtra(PeopleShowActivity.EXTRA_URL, peopleData.getUrl());
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