package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.ConnectionApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class EventsShowFragment extends ApplicationFragment{
    private String link;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(container == null)
            return null;
        
        View view = inflater.inflate(R.layout.events_show_fragment, container, false);
        
        if(!link.isEmpty())
            loadEvent();
        
        return view;
    }
    
    private void loadEvent(){
        if(ConnectionApi.isConnection(getActivity()))
            new LoadEvent().execute();
    }
    
    private class LoadEvent extends AsyncTask<Void, Void, Void>{
        private String title;
        private String description;
        private String placeInfo;
        private String priceInfo;
        private String linkInfo;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect(link).get();

                Element titleElement = document.select("h1.title").first();
                Element descriptionElement = document.select("div.description").first();
                Elements blockInfo = document.select("div.info_block > dl > dd");

                int i = 1;
                for(Element info : blockInfo){
                    if(i == 1){
                        placeInfo = info.text();
                    }else if(i == 2){
                        priceInfo = info.text();
                    }else{
                        linkInfo = info.outerHtml();
                    }
                    i++;
                }

                title = titleElement.text();

                /*
                 * http://stackoverflow.com/questions/3961589/android-webview-and-loaddata If you now how solve this problem an another way
                 * please sumbit a patch.
                 */
                description = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                if(preferences.isUseCSS())
                    description += "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";
                description += descriptionElement.outerHtml();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled()){
                TextView titleView = (TextView) getActivity().findViewById(R.id.title);
                TextView placeInfoView = (TextView) getActivity().findViewById(R.id.place_info);
                TextView priceInfoView = (TextView) getActivity().findViewById(R.id.price_info);
                TextView linkInfoView = (TextView) getActivity().findViewById(R.id.link_info);
                WebView descriptionView = (WebView) getActivity().findViewById(R.id.description);

                titleView.setText(title);
                placeInfoView.setText(placeInfo);
                priceInfoView.setText(priceInfo);

                linkInfoView.setText(Html.fromHtml(linkInfo));
                linkInfoView.setMovementMethod(LinkMovementMethod.getInstance());

                descriptionView.getSettings().setPluginsEnabled(true);
                descriptionView.getSettings().setSupportZoom(true);
                descriptionView.getSettings().setBuiltInZoomControls(true);
                descriptionView.loadDataWithBaseURL("", description, "text/html", "UTF-8", null);
            }
        }

    }
    
    public void setLink(String link){
        this.link = link;
    }
    
    public String getLink(){
        return link;
    }
    
}