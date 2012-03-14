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

package net.meiolania.apps.habrahabr.ui.companies;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.ApplicationListFragment;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class CompaniesFragment extends ApplicationListFragment implements OnScrollListener{
    protected final ArrayList<CompaniesData> companiesDataList = new ArrayList<CompaniesData>();
    protected CompaniesAdapter companiesAdapter;
    protected int page = 0;
    protected boolean canLoadingData = true;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        loadList();
    }
    
    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        showCompany(position);
    }
    
    protected void showCompany(int position){
        CompaniesData companiesData = companiesDataList.get(position);
        
        if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign()){
            getListView().setItemChecked(position, true);
            
            CompaniesShowFragment companiesShowFragment = (CompaniesShowFragment) getFragmentManager().findFragmentById(R.id.companies_show_fragment);
            
            if(companiesShowFragment == null || !companiesShowFragment.getLink().equals(companiesData.getLink())){
                companiesShowFragment = new CompaniesShowFragment();
                companiesShowFragment.setLink(companiesData.getLink());
                
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.companies_show_fragment, companiesShowFragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }
        }else{
            Intent intent = new Intent(getActivity(), CompaniesShowActivity.class);
            intent.putExtra("link", companiesData.getLink());

            startActivity(intent);
        }
    }
    
    protected void loadList(){
        if(ConnectionApi.isConnection(getActivity())){
            ++page;
            new LoadCompaniesList().execute();
        }
    }
    
    private class LoadCompaniesList extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                getApi().getCompaniesApi().getCompanies(companiesDataList, page);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                companiesAdapter = new CompaniesAdapter(getActivity(), companiesDataList);
                setListAdapter(companiesAdapter);
                
                if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign()){
                    getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    showCompany(0);
                }    
                
                getListView().setOnScrollListener(CompaniesFragment.this);
            }else
                companiesAdapter.notifyDataSetChanged();
            canLoadingData = true;
        }

    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if((firstVisibleItem + visibleItemCount) == totalItemCount && page != 0 && canLoadingData){
            canLoadingData = false;
            loadList();
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState){}
    
}