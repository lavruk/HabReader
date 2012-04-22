package net.meiolania.apps.habrahabr.fragments;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.adapters.EventsAdapter;
import net.meiolania.apps.habrahabr.data.EventsData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public abstract class AbstractionEventsFragment extends SherlockListFragment implements OnScrollListener{
    public final static String LOG_TAG = "EventsFragment";
    protected final ArrayList<EventsData> eventsDatas = new ArrayList<EventsData>();
    protected EventsAdapter eventsAdapter;
    protected int page = 0;
    protected boolean loadMoreData = true;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        eventsAdapter = new EventsAdapter(getSherlockActivity(), eventsDatas);
        setListAdapter(eventsAdapter);
        getListView().setOnScrollListener(this);
    }

    protected void loadList(){
        ++page;
        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
        new LoadEvents().execute();
    }

    protected abstract String getUrl();

    protected final class LoadEvents extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                Log.i(LOG_TAG, "Loading " + String.format(getUrl(), page));

                Document document = Jsoup.connect(String.format(getUrl(), page)).get();
                Elements events = document.select("div.event");
                for(Element event : events){
                    EventsData eventsData = new EventsData();
                    
                    Element title = event.select("h1.title").first();
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