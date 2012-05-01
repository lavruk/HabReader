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

package net.meiolania.apps.habrahabr.fragments;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.CompaniesShowActivity;
import net.meiolania.apps.habrahabr.adapters.CompaniesAdapter;
import net.meiolania.apps.habrahabr.data.CompaniesData;
import net.meiolania.apps.habrahabr.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;

public class CompaniesFragment extends SherlockListFragment implements OnScrollListener{
    public final static String LOG_TAG = "CompaniesFragment";
    public final static String URL = "http://habrahabr.ru/companies/page%d/";
    protected final ArrayList<CompaniesData> companiesDatas = new ArrayList<CompaniesData>();
    protected CompaniesAdapter companiesAdapter;
    protected boolean loadMoreData = true;
    protected int page = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        companiesAdapter = new CompaniesAdapter(getSherlockActivity(), companiesDatas);
        setListAdapter(companiesAdapter);
        getListView().setOnScrollListener(this);
    }

    protected void loadList(){
        ++page;
        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
        new LoadCompanies().execute();
    }

    protected final class LoadCompanies extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                Log.i(LOG_TAG, "Loading " + String.format(URL, page));
                
                Document document = Jsoup.connect(String.format(URL, page)).get();
                Elements companies = document.select("div.company");
                
                if(companies.size() <= 0){
                    loadMoreData = false;
                    /*
                     * It's a solve for:
                     * java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
                     */
                    getSherlockActivity().runOnUiThread(new Runnable(){
                        public void run(){
                            Toast.makeText(getSherlockActivity(), R.string.no_more_pages, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                
                for(Element company : companies){
                    CompaniesData companiesData = new CompaniesData();
                    
                    Element icon = company.select("div.icon > img").first();
                    Element index = company.select("div.habraindex").first();
                    Element title = company.select("div.description > div.name > a").first();
                    Element description = company.select("div.description > p").first();
                    
                    companiesData.setTitle(title.text());
                    companiesData.setUrl(title.attr("abs:href"));
                    //TODO: rewrite the code below
                    companiesData.setIcon("http://habrahabr.ru" + icon.attr("src"));
                    companiesData.setIndex(index.text());
                    companiesData.setDescription(description.text());
                    
                    companiesDatas.add(companiesData);
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
                        companiesAdapter.notifyDataSetChanged();
                    loadMoreData = true;
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                }
            });
        }

    }
    
    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        showCompany(position);
    }

    protected void showCompany(int position){
        CompaniesData companiesData = companiesDatas.get(position);
        Intent intent = new Intent(getSherlockActivity(), CompaniesShowActivity.class);
        intent.putExtra(CompaniesShowActivity.EXTRA_TITLE, companiesData.getTitle());
        intent.putExtra(CompaniesShowActivity.EXTRA_URL, companiesData.getUrl());
        startActivity(intent);
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if((firstVisibleItem + visibleItemCount) == totalItemCount && loadMoreData){
            loadMoreData = false;
            loadList();
            Log.i(LOG_TAG, "Loading " + page + " page");
            //TODO: need to find a better way to display a notification for devices with Android < 3.0
            if(!UIUtils.isHoneycombOrHigher())
                Toast.makeText(getSherlockActivity(), getString(R.string.loading_page, page), Toast.LENGTH_SHORT).show();
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState){

    }

}