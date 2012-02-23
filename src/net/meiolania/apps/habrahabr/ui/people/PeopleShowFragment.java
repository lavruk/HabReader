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

package net.meiolania.apps.habrahabr.ui.people;

import java.io.IOException;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.ui.fragments.ApplicationFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class PeopleShowFragment extends ApplicationFragment{
    private String link;
    private boolean isFullView = false;
    private LoadUser loadUser;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        
        if(link != null && link.length() > 0)
            loadUser();
    }
    
    @Override
    public void onDestroy(){
        super.onDestroy();
        
        if(loadUser != null)
            loadUser.cancel(true);
    }
    
    public void setIsFullView(boolean isFullView){
        this.isFullView = isFullView;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(container == null)
            return null;

        return inflater.inflate(R.layout.people_show_fragment, container, false);
    }
    
    private void loadUser(){
        if(ConnectionApi.isConnection(getActivity())){
            loadUser = new LoadUser();
            loadUser.execute();
        }
    }
    
    private class LoadUser extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;
        //Data
        private String userName;
        private String ratingPlace;
        private String birthday;
        private String place;
        private String summary;
        private String tags;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Log.d("meiolania", link);

                Document document = Jsoup.connect(link).get();

                Element userNameElement = document.select("dl.user-name > dt.fn").first();
                Element ratingPlaceElement = document.select("dl.user-name > dd.rating-place").first();
                Element birthdayElement = document.select("dd.bday").first();
                // Place
                Element countryElement = document.select("a.country-name").first();
                Element regionElement = document.select("a.region").first();
                Element cityElement = document.select("city").first();
                // Others
                Element summaryElement = document.select("dd.summary").first();
                Element tagsElement = document.select("ul#people-tags").first();

                userName = userNameElement.text();
                ratingPlace = ratingPlaceElement.text();
                
                birthday = birthdayElement.text();

                StringBuilder placeBuilder = new StringBuilder();

                if(countryElement != null && countryElement.hasText()){
                    placeBuilder.append(countryElement.text());
                    if(regionElement != null && regionElement.hasText())
                        placeBuilder.append(", " + regionElement.text());
                    if(cityElement != null && cityElement.hasText())
                        placeBuilder.append(", " + cityElement.text());
                }else
                    placeBuilder.append(getString(R.string.place_undefined));

                place = placeBuilder.toString();

                summary = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                if(preferences.isVibrate())
                    summary += "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";
                summary += summaryElement != null ? summaryElement.outerHtml() : "";

                if(preferences.isVibrate())
                    tags = "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";
                tags += tagsElement != null ? tagsElement.outerHtml() : "";
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
                progressDialog.setMessage(getString(R.string.loading_man_show));
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled()){
                TextView userNameView = (TextView) getActivity().findViewById(R.id.name);
                TextView ratingPlaceView = (TextView) getActivity().findViewById(R.id.rating_place);
                TextView birthdayView = (TextView) getActivity().findViewById(R.id.birthday);
                TextView placeView = (TextView) getActivity().findViewById(R.id.place);
                TextView tagsView = (TextView) getActivity().findViewById(R.id.tags);
                WebView summaryView = (WebView) getActivity().findViewById(R.id.summary);

                userNameView.setText((userName != null) ? userName : getString(R.string.no_info));
                ratingPlaceView.setText((ratingPlace != null) ? ratingPlace : getString(R.string.no_info));
                birthdayView.setText((birthday != null) ? birthday : getString(R.string.no_info));
                placeView.setText((place != null) ? place : getString(R.string.no_info));

                tagsView.setText((tags != null) ? Html.fromHtml(tags) : getString(R.string.no_info));
                tagsView.setMovementMethod(LinkMovementMethod.getInstance());

                summaryView.getSettings().setPluginsEnabled(true);
                summaryView.getSettings().setSupportZoom(true);
                summaryView.getSettings().setBuiltInZoomControls(true);
                summaryView.loadDataWithBaseURL("", summary, "text/html", "UTF-8", null);
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