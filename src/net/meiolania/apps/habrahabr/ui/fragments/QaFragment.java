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
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.QaAdapter;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.data.QAData;
import net.meiolania.apps.habrahabr.ui.activities.QaShowActivity;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class QaFragment extends ApplicationListFragment implements OnScrollListener{
    protected final ArrayList<QAData> qaDataList = new ArrayList<QAData>();
    protected QaAdapter qaAdapter;
    protected int page;
    protected boolean canLoadingData = true;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        
        getListView().setOnScrollListener(this);
        
        loadList();

        if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign())
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    protected void loadList(){
        if(ConnectionApi.isConnection(getActivity())){
            ++page;
            new LoadQAList().execute();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        showQa(position);
    }

    protected void showQa(int position){
        QAData qaData = qaDataList.get(position);

        if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign()){
            getListView().setItemChecked(position, true);

            QaShowFragment qaShowFragment = (QaShowFragment) getFragmentManager().findFragmentById(R.id.qa_show_fragment);

            if(qaShowFragment == null || qaShowFragment.getLink() != qaData.getLink()){
                qaShowFragment = new QaShowFragment();
                qaShowFragment.setLink(qaData.getLink());

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.qa_show_fragment, qaShowFragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }
        }else{
            Intent intent = new Intent(getActivity(), QaShowActivity.class);
            intent.putExtra("link", qaData.getLink());

            startActivity(intent);
        }
    }

    protected class LoadQAList extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                getApi().getQaApi().getQa(qaDataList, page);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                qaAdapter = new QaAdapter(getActivity(), qaDataList);
                setListAdapter(qaAdapter);

                if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign())
                    showQa(0);
            }else
                qaAdapter.notifyDataSetChanged();
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