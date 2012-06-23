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

package net.meiolania.apps.habrahabr.fragments.companies;

import java.io.IOException;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.CompaniesFullData;

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
import android.webkit.WebView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class CompaniesShowFragment extends SherlockFragment{
    public final static String LOG_TAG = "CompaniesShowFragment";
    public final static int INFO_DATE = 0;
    public final static int INFO_SITE = 1;
    public final static int INFO_INDUSTRIES = 2;
    public final static int INFO_LOCATION = 3;
    public final static int INFO_QUANTITY = 4;
    public final static int INFO_SUMMARY = 5;
    public final static int INFO_MANAGEMENT = 6;
    public final static int INFO_DEVELOPMENT_STAGES = 7;
    protected String url;

    public CompaniesShowFragment(){
    }

    public CompaniesShowFragment(String url){
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
        loadInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.companies_show_activity, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.companies_show_activity, menu);
    }

    protected void loadInfo(){
        new LoadCompany().execute();
    }

    protected final class LoadCompany extends AsyncTask<Void, Void, CompaniesFullData>{
        private ProgressDialog progressDialog;

        @Override
        protected CompaniesFullData doInBackground(Void... params){
            CompaniesFullData companiesFullData = new CompaniesFullData();
            try{
                Log.i(LOG_TAG, "Loading " + url);

                Document document = Jsoup.connect(url).get();
                Elements datas = document.select("div.company_profile > dl");
                int i = 0;
                for(Element data : datas){
                    switch(i){
                        case INFO_DATE:
                            companiesFullData.setDate(data.getElementsByTag("dd").first().text());
                            break;
                        case INFO_SITE:
                            companiesFullData.setCompanyUrl(data.getElementsByTag("dd").first().text());
                            break;
                        case INFO_INDUSTRIES:
                            companiesFullData.setIndustries(data.getElementsByTag("dd").first().text());
                            break;
                        case INFO_LOCATION:
                            companiesFullData.setLocation(data.getElementsByTag("dd").first().text());
                            break;
                        case INFO_QUANTITY:
                            companiesFullData.setQuantity(data.getElementsByTag("dd").first().text());
                            break;
                        case INFO_SUMMARY:
                            companiesFullData.setSummary(data.select("dd.summary").first().html());
                            break;
                        case INFO_MANAGEMENT:
                            //TODO: think of a new algorithm
                            Elements managers = data.getElementsByTag("dd");
                            StringBuilder managerContent = new StringBuilder();
                            for(Element manager : managers)
                                managerContent.append(manager.html());
                            companiesFullData.setManagement(managerContent.toString());
                            break;
                        case INFO_DEVELOPMENT_STAGES:
                            Elements stages = data.getElementsByTag("dd");
                            StringBuilder stagesContent = new StringBuilder();
                            for(Element stage : stages)
                                stagesContent.append(stage.html());
                            companiesFullData.setDevelopmentStages(stagesContent.toString());
                            break;
                    }
                    i++;
                }
            }
            catch(IOException e){
            }
            return companiesFullData;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(getSherlockActivity());
            progressDialog.setTitle(R.string.loading);
            progressDialog.setMessage(getString(R.string.loading_company));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(final CompaniesFullData result){
            getSherlockActivity().runOnUiThread(new Runnable(){
                public void run(){
                    if(!isCancelled()){
                        SherlockFragmentActivity activity = getSherlockActivity();
                        
                        TextView date = (TextView)activity.findViewById(R.id.company_date);
                        date.setText(result.getDate());
                        
                        TextView site = (TextView)activity.findViewById(R.id.company_site);
                        site.setText(result.getCompanyUrl());
                        
                        TextView industries = (TextView)activity.findViewById(R.id.company_industries);
                        industries.setText(result.getIndustries());
                        
                        TextView location = (TextView)activity.findViewById(R.id.company_location);
                        location.setText(result.getLocation());
                        
                        TextView quantity = (TextView)activity.findViewById(R.id.company_quantity);
                        quantity.setText(result.getQuantity());
                        
                        WebView summary = (WebView)activity.findViewById(R.id.company_summary);
                        summary.getSettings().setSupportZoom(true);
                        summary.loadDataWithBaseURL("", result.getSummary(), "text/html", "UTF-8", null);
                        
                        WebView management = (WebView)activity.findViewById(R.id.company_management);
                        management.getSettings().setSupportZoom(true);
                        management.loadDataWithBaseURL("", result.getManagement(), "text/html", "UTF-8", null);
                        
                        WebView developmentStages = (WebView)activity.findViewById(R.id.company_development_stages);
                        developmentStages.getSettings().setSupportZoom(true);
                        developmentStages.loadDataWithBaseURL("", result.getDevelopmentStages(), "text/html", "UTF-8", null);
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }

}