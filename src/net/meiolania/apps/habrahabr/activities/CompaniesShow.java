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

package net.meiolania.apps.habrahabr.activities;

import java.io.IOException;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.utils.Vibrate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;

public class CompaniesShow extends ApplicationActivity{
    private String link;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companies_show);

        Bundle extras = getIntent().getExtras();
        link = extras.getString("link");

        setActionBar();
        loadCompany();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.companies_show, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(Preferences.vibrate)
            Vibrate.doVibrate(this);
        switch(item.getItemId()){
            case R.id.show_in_browser:
                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.to_home:
                startActivity(new Intent(this, Dashboard.class));
                break;    
        }
        return true;
    }

    private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.companies);
    }

    private void loadCompany(){
        new LoadCompany().execute();
    }
    
    //TODO: need to rewrite
    private class LoadCompany extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;
        private String title;
        private String date;
        private String linkToCompany;
        private String industry;
        private String place;
        private String staff;
        private String aboutCompany;
        private String leadership;
        private String stages;
        //Type of an information
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
                        
                        if(Preferences.useCSS)
                            builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />");
                        
                        for(Element stage : stagesElements)
                            builder.append(stage.outerHtml());
                                
                        stages = builder.toString();        
                    }
                        
                    i++;
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(CompaniesShow.this);
            progressDialog.setMessage(getString(R.string.loading_companies_show));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled()){
                TextView titleView = (TextView)findViewById(R.id.title);
                titleView.setText(title);
                
                TextView dateView = (TextView)findViewById(R.id.date);
                dateView.setText(date);
                
                TextView linkView = (TextView)findViewById(R.id.link);
                linkView.setText(Html.fromHtml(linkToCompany));
                linkView.setMovementMethod(LinkMovementMethod.getInstance());
                
                TextView industryView = (TextView)findViewById(R.id.industry);
                industryView.setText(industry);
                
                TextView placeView = (TextView)findViewById(R.id.place);
                placeView.setText(place);
                
                TextView staffView = (TextView)findViewById(R.id.staff);
                staffView.setText(staff);
                
                TextView aboutCompanyView = (TextView)findViewById(R.id.about_company);
                aboutCompanyView.setText(aboutCompany);
                
                TextView leadershipView = (TextView)findViewById(R.id.leadership);
                leadershipView.setText(Html.fromHtml(leadership));
                
                WebView stagesView = (WebView)findViewById(R.id.stages);
                stagesView.getSettings().setPluginsEnabled(true);
                stagesView.getSettings().setSupportZoom(true);
                stagesView.getSettings().setBuiltInZoomControls(true);
                stagesView.loadData(stages, "text/html", "UTF-8");
            }
            progressDialog.dismiss();
        }

    }

}