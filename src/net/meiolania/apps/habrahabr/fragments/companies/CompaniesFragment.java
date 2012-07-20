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

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.activities.CompaniesShowActivity;
import net.meiolania.apps.habrahabr.adapters.CompaniesAdapter;
import net.meiolania.apps.habrahabr.data.CompaniesData;
import net.meiolania.apps.habrahabr.fragments.companies.loader.CompaniesLoader;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class CompaniesFragment extends SherlockListFragment implements OnScrollListener, LoaderCallbacks<ArrayList<CompaniesData>>{
	public final static int LOADER_COMPANIES = 0;
    protected ArrayList<CompaniesData> companiesDatas;
    protected CompaniesAdapter companiesAdapter;
    private int page;
    private boolean isLoadData;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        
        setRetainInstance(true);
        
        if(companiesAdapter == null){
        	companiesDatas = new ArrayList<CompaniesData>();
        	companiesAdapter = new CompaniesAdapter(getSherlockActivity(), companiesDatas);
        }
        
        setListAdapter(companiesAdapter);
        setListShown(true);
        
        getListView().setOnScrollListener(this);
    }
    
    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        showCompany(position);
    }

    protected void showCompany(int position){
        CompaniesData companiesData = companiesDatas.get(position);
        
        Intent intent = new Intent(getSherlockActivity(), CompaniesShowActivity.class);
        intent.putExtra(CompaniesShowActivity.EXTRA_TITLE, companiesData.getTitle());
        //TODO: made a quick fix. Need to make another.
        intent.putExtra(CompaniesShowActivity.EXTRA_URL, companiesData.getUrl() + "/profile/");
        
        startActivity(intent);
    }
    
    protected void restartLoading(){
    	getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
    	
    	CompaniesLoader.setPage(++page);
    	
    	getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_COMPANIES, null, this);
    	
    	isLoadData = true;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if((firstVisibleItem + visibleItemCount) == totalItemCount && !isLoadData)
        	restartLoading();
    }

    public void onScrollStateChanged(AbsListView view, int scrollState){

    }

	@Override
	public Loader<ArrayList<CompaniesData>> onCreateLoader(int id, Bundle args){
		CompaniesLoader loader = new CompaniesLoader(getSherlockActivity());
		loader.forceLoad();
		
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<CompaniesData>> loader, ArrayList<CompaniesData> data){
		companiesDatas.addAll(data);
		companiesAdapter.notifyDataSetChanged();
		
		if(getSherlockActivity() != null)
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
		
		isLoadData = false;
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<CompaniesData>> loader){
		
	}

}