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

package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.ConnectionApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

public class CompaniesShowFragment extends ApplicationFragment{
    private String link;
    private boolean isFullView = false;
    private LoadCompany loadCompany;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        
        if(link != null && link.length() > 0)
            loadCompany();
    }
    
    @Override
    public void onDestroy(){
        super.onDestroy();
        
        if(loadCompany != null)
            loadCompany.cancel(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(container == null)
            return null;

        return inflater.inflate(R.layout.companies_show_fragment, container, false);
    }
    
    public void setIsFullView(boolean isFullView){
        this.isFullView = isFullView;
    }

    private void loadCompany(){
        if(ConnectionApi.isConnection(getActivity())){
            loadCompany = new LoadCompany();
            loadCompany.execute();
        }    
    }

    // TODO: need to rewrite
    private class LoadCompany extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;
        //Data
        private String title;
        private String date;
        private String linkToCompany;
        private String industry;
        private String place;
        private String staff;
        private String aboutCompany;
        private String leadership;
        private String stages;
        // Type of an information
        private final static int INFO_DATE = 1;
        private final static int INFO_LINK = 2;
        private final static int INFO_INDUSTRY = 3;
        private final static int INFO_PLACE = 4;
        private final static int INFO_STAFF = 5;
        private final static int INFO_ABOUT_COMPANY = 6;
        private final static int INFO_LEADERSHIP = 7;
        private final static int INFO_STAGES = 8;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect(link).get();

                Element favicon = document.select("img.favicon").first();
                Elements companyInfo = document.select("div.userinfo > dl");

                title = favicon.attr("alt");

                int i = 1;
                for(Element info : companyInfo){
                    if(i == INFO_DATE)
                        date = info.getElementsByTag("dd").first().text();
                    else if(i == INFO_LINK)
                        linkToCompany = info.select("dd.url").first().outerHtml();
                    else if(i == INFO_INDUSTRY)
                        industry = info.getElementsByTag("dd").first().text();
                    else if(i == INFO_PLACE)
                        place = info.getElementsByTag("dd").first().text();
                    else if(i == INFO_STAFF)
                        staff = info.getElementsByTag("dd").first().text();
                    else if(i == INFO_ABOUT_COMPANY)
                        aboutCompany = info.select("dd.summary").first().text();
                    else if(i == INFO_LEADERSHIP){
                        StringBuilder builder = new StringBuilder();
                        Elements leadershipElements = info.getElementsByTag("dd");

                        for(Element leader : leadershipElements)
                            builder.append(leader.outerHtml());

                        leadership = builder.toString();
                    }else if(i == INFO_STAGES){
                        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
                        Elements stagesElements = info.getElementsByTag("dd");

                        if(preferences.isUseCSS())
                            builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />");

                        for(Element stage : stagesElements)
                            builder.append(stage.outerHtml());

                        stages = builder.toString();
                    }

                    i++;
                }
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
                progressDialog.setMessage(getString(R.string.loading_companies_show));
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled()){
                TextView titleView = (TextView) getActivity().findViewById(R.id.title);
                titleView.setText((title != null) ? title : getString(R.string.no_info));

                TextView dateView = (TextView) getActivity().findViewById(R.id.date);
                dateView.setText((date != null) ? date : getString(R.string.no_info));

                TextView linkView = (TextView) getActivity().findViewById(R.id.link);
                linkView.setText((linkToCompany != null) ? Html.fromHtml(linkToCompany) : getString(R.string.no_info));
                linkView.setMovementMethod(LinkMovementMethod.getInstance());

                TextView industryView = (TextView) getActivity().findViewById(R.id.industry);
                industryView.setText((industry != null) ? industry : getString(R.string.no_info));

                TextView placeView = (TextView) getActivity().findViewById(R.id.place);
                placeView.setText((place != null) ? place : getString(R.string.no_info));

                TextView staffView = (TextView) getActivity().findViewById(R.id.staff);
                staffView.setText((staff != null) ? staff : getString(R.string.no_info));

                TextView aboutCompanyView = (TextView) getActivity().findViewById(R.id.about_company);
                aboutCompanyView.setText((aboutCompany != null) ? aboutCompany : getString(R.string.no_info));

                TextView leadershipView = (TextView) getActivity().findViewById(R.id.leadership);
                leadershipView.setText((leadership != null) ? Html.fromHtml(leadership) : getString(R.string.no_info));

                WebView stagesView = (WebView) getActivity().findViewById(R.id.stages);
                stagesView.getSettings().setPluginsEnabled(true);
                stagesView.getSettings().setSupportZoom(true);
                stagesView.getSettings().setBuiltInZoomControls(true);
                stagesView.loadDataWithBaseURL("", stages, "text/html", "UTF-8", null);
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