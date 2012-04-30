package net.meiolania.apps.habrahabr.fragments;

import java.io.IOException;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.EventsFullData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class EventsShowFragment extends SherlockFragment{
    public final static int INFO_LOCATION = 0;
    public final static int INFO_PAY = 1;
    public final static int INFO_SITE = 2;
    protected String url;

    public EventsShowFragment(){
    }

    public EventsShowFragment(String url){
        this.url = url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        loadEvent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.events_show_activity, container, false);
    }

    protected void loadEvent(){
        Log.d("EventsShowFragment", "Url is " + url);
        new LoadEvent().execute();
    }

    protected final class LoadEvent extends AsyncTask<Void, Void, EventsFullData>{
        private ProgressDialog progressDialog;

        @Override
        protected EventsFullData doInBackground(Void... params){
            EventsFullData eventsFullData = new EventsFullData();
            try{
                Document document = Jsoup.connect(url).get();
                Element day = document.select("div.date > div.day").first();
                Element month = document.select("div.date > div.month").first();
                Element logo = document.select("div.logo > img").first();
                Element title = document.select("div.info_block > h1.title").first();
                Element detail = document.select("div.info_block > div.detail").first();
                Element description = document.select("div.info_block > div.description").first();
                Elements additionalInfo = document.select("div.info_block").first().getElementsByTag("dd");
                
                Element location = null, pay = null, site = null;
                //TODO: need to test this code more
                int i = 0;
                for(Element info : additionalInfo){
                    switch(i){
                        case INFO_LOCATION:
                            location = info;
                            break;
                        case INFO_PAY:
                            pay = info;
                            break;    
                        case INFO_SITE:
                            site = info;
                            break;    
                    }
                    i++;
                }
                
                eventsFullData.setTitle(title.text());
                eventsFullData.setUrl(url);
                eventsFullData.setDate(day.text() + " " + month.text());
                //eventsFullData.setLogo(logo.attr("src"));
                eventsFullData.setDetail(detail.text());
                eventsFullData.setText(description.text());
                eventsFullData.setPay(pay.text());
                eventsFullData.setLocation(location.text());
                eventsFullData.setSite(site.text());
            }
            catch(IOException e){
            }
            return eventsFullData;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(getSherlockActivity());
            progressDialog.setTitle(R.string.loading);
            progressDialog.setMessage(getString(R.string.loading_event));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(final EventsFullData result){
            getSherlockActivity().runOnUiThread(new Runnable(){
                public void run(){
                    if(!isCancelled()){
                        SherlockFragmentActivity activity = getSherlockActivity();
                        
                        TextView title = (TextView)activity.findViewById(R.id.event_title);
                        title.setText(result.getTitle());
                        
                        TextView location = (TextView)activity.findViewById(R.id.event_location);
                        location.setText(result.getLocation());
                        
                        TextView pay = (TextView)activity.findViewById(R.id.event_pay);
                        pay.setText(result.getPay());
                        
                        TextView site = (TextView)activity.findViewById(R.id.event_site);
                        site.setText(result.getSite());
                        
                        TextView description = (TextView)activity.findViewById(R.id.event_description);
                        description.setText(result.getText());
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }

}