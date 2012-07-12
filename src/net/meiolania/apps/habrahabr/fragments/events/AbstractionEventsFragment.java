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

package net.meiolania.apps.habrahabr.fragments.events;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.EventsShowActivity;
import net.meiolania.apps.habrahabr.adapters.EventsAdapter;
import net.meiolania.apps.habrahabr.data.EventsData;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import net.meiolania.apps.habrahabr.utils.UIUtils;

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
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public abstract class AbstractionEventsFragment extends SherlockListFragment implements OnScrollListener{
    public final static String LOG_TAG = "EventsFragment";
    protected final ArrayList<EventsData> eventsDatas = new ArrayList<EventsData>();
    protected EventsAdapter eventsAdapter;
    protected int page = 0;
    protected boolean loadMoreData = true;
    protected boolean noMorePages = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        eventsAdapter = new EventsAdapter(getSherlockActivity(), eventsDatas);
        setListAdapter(eventsAdapter);
        getListView().setOnScrollListener(this);
    }

    protected void loadList(){
    	if(ConnectionUtils.isConnected(getSherlockActivity())){
    		++page;
            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
            new LoadEvents().execute();
    	}
    }

    protected abstract String getUrl();

    protected final class LoadEvents extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                Log.i(LOG_TAG, "Loading " + getUrl().replace("%page%", String.valueOf(page)));

                Document document = Jsoup.connect(String.format(getUrl().replace("%page%", String.valueOf(page)))).get();
                Elements events = document.select("div.event");
                
                if(events.size() <= 0){
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
                
                for(Element event : events){
                    EventsData eventsData = new EventsData();
                    
                    Element title = event.select("h1.title > a").first();
                    Element detail = event.select("div.detail").first();
                    Element text = event.select("div.text").first();
                    
                    eventsData.setTitle(title.text());
                    eventsData.setUrl(title.attr("abs:href"));
                    eventsData.setDetail(detail.text());
                    eventsData.setText(text.text());
                    
                    eventsDatas.add(eventsData);
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
                        eventsAdapter.notifyDataSetChanged();
                    loadMoreData = true;
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                }
            });
        }

    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        showEvent(position);
    }

    protected void showEvent(int position){
        EventsData eventsData = eventsDatas.get(position);
        Intent intent = new Intent(getSherlockActivity(), EventsShowActivity.class);
        intent.putExtra(EventsShowActivity.EXTRA_TITLE, eventsData.getTitle());
        intent.putExtra(EventsShowActivity.EXTRA_URL, eventsData.getUrl());
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