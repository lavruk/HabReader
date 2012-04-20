package net.meiolania.apps.habrahabr.fragments;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.adapters.HubsAdapter;
import net.meiolania.apps.habrahabr.data.HubsData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.actionbarsherlock.app.SherlockListFragment;

public class HubsFragment extends SherlockListFragment implements OnScrollListener{
    public final static String LOG_TAG = "HubsFragment";
    protected final ArrayList<HubsData> hubsDatas = new ArrayList<HubsData>();
    protected HubsAdapter hubsAdapter;
    protected int page = 0;
    protected boolean loadMoreData = true;
    protected boolean noMorePages = false;
    protected String url;

    public HubsFragment(){
    }

    public HubsFragment(String url){
        this.url = url;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        hubsAdapter = new HubsAdapter(getSherlockActivity(), hubsDatas);
        setListAdapter(hubsAdapter);
        getListView().setOnScrollListener(this);
    }

    protected void loadList(){
        ++page;
        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
        new LoadHubs().execute();
    }

    protected final class LoadHubs extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                Log.i(LOG_TAG, "Loading " + String.format(url, page));

                Document document = Jsoup.connect(String.format(url, page)).get();
                
                Element pageNavigation = document.select("div.page-nav").first();
                if(pageNavigation == null)
                    noMorePages = true;
                
                Elements hubs = document.select("div.hub");
                for(Element hub : hubs){
                    HubsData hubsData = new HubsData();
                    
                    Element index = hub.select("div.habraindex").first();
                    Element category = hub.select("div.category").first();
                    Element title = hub.select("div.title > a").first();
                    Element stat = hub.select("div.stat").first();
                    
                    hubsData.setTitle(title.text());
                    hubsData.setUrl(title.attr("abs:href"));
                    hubsData.setCategory(category.text());
                    hubsData.setStat(stat.text());
                    hubsData.setIndex(index.text());
                    
                    hubsDatas.add(hubsData);
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
                        hubsAdapter.notifyDataSetChanged();
                    loadMoreData = true;
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                }
            });
        }

    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if((firstVisibleItem + visibleItemCount) == totalItemCount && loadMoreData && !noMorePages){
            loadMoreData = false;
            loadList();
            Log.i(LOG_TAG, "Loading " + page + " page");
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState){

    }

}