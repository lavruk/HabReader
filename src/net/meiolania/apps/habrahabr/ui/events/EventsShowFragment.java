/*
   Copyright (C) 2011 Andrey Zaytsev <a.einsam@gmail.com>
  
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

package net.meiolania.apps.habrahabr.ui.events;

import java.io.IOException;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.ui.fragments.ApplicationFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
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
    private boolean isFullView = false;
    private LoadEvent loadEvent;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        
        if(link != null && link.length() > 0)
            loadEvent();
    }
    
    @Override
    public void onDestroy(){
        super.onDestroy();
        
        if(loadEvent != null)
            loadEvent.cancel(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(container == null)
            return null;

        return inflater.inflate(R.layout.events_show_fragment, container, false);
    }
    
    public void setIsFullView(boolean isFullView){
        this.isFullView = isFullView;
    }

    private void loadEvent(){
        if(ConnectionApi.isConnection(getActivity())){
            loadEvent = new LoadEvent();
            loadEvent.execute();
        }
    }

    private class LoadEvent extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;
        //Data
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
                
            }
            catch(NullPointerException e){
                
            }
            return null;
        }
        
        @Override
        protected void onPreExecute(){
            if(isFullView){
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.loading_events_show));
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled()){
                TextView titleView = (TextView) getActivity().findViewById(R.id.title);
                if(titleView != null){
                    TextView placeInfoView = (TextView) getActivity().findViewById(R.id.place_info);
                    TextView priceInfoView = (TextView) getActivity().findViewById(R.id.price_info);
                    TextView linkInfoView = (TextView) getActivity().findViewById(R.id.link_info);
                    WebView descriptionView = (WebView) getActivity().findViewById(R.id.description);

                    titleView.setText((title != null) ? title : getString(R.string.no_info));
                    placeInfoView.setText((placeInfo != null) ? placeInfo : getString(R.string.no_info));
                    priceInfoView.setText((priceInfo != null) ? priceInfo : getString(R.string.no_info));

                    linkInfoView.setText((linkInfo != null) ? Html.fromHtml(linkInfo) : getString(R.string.no_info));
                    linkInfoView.setMovementMethod(LinkMovementMethod.getInstance());

                    descriptionView.getSettings().setPluginsEnabled(true);
                    descriptionView.getSettings().setSupportZoom(true);
                    descriptionView.getSettings().setBuiltInZoomControls(true);
                    descriptionView.loadDataWithBaseURL("", description, "text/html", "UTF-8", null);
                }
            }
            if(isFullView)
                progressDialog.dismiss();
        }

    }

    public void setLink(String link){
        this.link = link;
    }

    public String getLink(){
        return link;
    }

}