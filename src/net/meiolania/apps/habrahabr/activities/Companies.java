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
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.CompaniesAdapter;
import net.meiolania.apps.habrahabr.data.CompaniesData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class Companies extends ApplicationActivity{
    private final ArrayList<CompaniesData> companiesDataList = new ArrayList<CompaniesData>();
    private CompaniesAdapter companiesAdapter;
    private int page;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companies);

        setActionBar();
        loadList();
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_SEARCH:
                startActivity(new Intent(this, CompaniesSearch.class));
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.companies);
        actionBar.addAction(new LoadNextPageAction());
        actionBar.addAction(new IntentAction(this, new Intent(this, CompaniesSearch.class), R.drawable.actionbar_ic_search));
    }
    
    private class LoadNextPageAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_forward;
        }

        public void performAction(View view){
            loadList();
        }
        
    }

    private void loadList(){
        ++page;
        new LoadCompaniesList().execute();
    }

    private class LoadCompaniesList extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect("http://habrahabr.ru/companies/page" + page + "/").get();
                Element tableCompanies = document.select("ul.corps-list").first();

                Elements dataCompanies = tableCompanies.select("li");
                
                boolean firstElement = true;
                for(Element company : dataCompanies){
                    /*
                     * We need to skip the first <li></li> because it hasn`t data
                     */
                    if(firstElement){
                        firstElement = false;
                        continue;
                    }
                    CompaniesData companiesData = new CompaniesData();
                    
                    Element logo = company.getElementsByTag("img").first();
                    
                    companiesData.setLogo(logo.attr("src"));
                    companiesData.setTitle(logo.attr("title"));
                    
                    companiesDataList.add(companiesData);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(Companies.this);
            progressDialog.setMessage(getString(R.string.loading_companies_list));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                companiesAdapter = new CompaniesAdapter(Companies.this, companiesDataList);
                
                ListView listView = (ListView)Companies.this.findViewById(R.id.companies_list);
                listView.setAdapter(companiesAdapter);
            }else
                companiesAdapter.notifyDataSetChanged();
            
            progressDialog.dismiss();
        }

    }

}