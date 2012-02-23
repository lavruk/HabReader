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
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.ui.fragments.ApplicationListFragment;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class EventsFragment extends ApplicationListFragment implements OnScrollListener{
    protected final ArrayList<EventsData> eventsDataList = new ArrayList<EventsData>();
    protected EventsAdapter eventsAdapter;
    protected int page;
    protected boolean canLoadingData = true;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        loadList();
    }
    
    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        showEvent(position);
    }
    
    protected void showEvent(int position){
        EventsData eventsData = eventsDataList.get(position);
        
        if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign()){
            getListView().setItemChecked(position, true);
            
            EventsShowFragment eventsShowFragment = (EventsShowFragment) getFragmentManager().findFragmentById(R.id.events_show_fragment);
            if(eventsShowFragment == null || !eventsShowFragment.getLink().equals(eventsData.getLink())){
                eventsShowFragment = new EventsShowFragment();
                eventsShowFragment.setLink(eventsData.getLink());
                
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.events_show_fragment, eventsShowFragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }  
        }else{
            Intent intent = new Intent(getActivity(), EventsShowActivity.class);
            intent.putExtra("link", eventsData.getLink());

            startActivity(intent);
        }
    }
    
    protected void loadList(){
        if(ConnectionApi.isConnection(getActivity())){
            ++page;
            new LoadEventsList().execute();
        }
    }
    
    private class LoadEventsList extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                getApi().getEventsApi().getEvents(eventsDataList, page);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                eventsAdapter = new EventsAdapter(getActivity(), eventsDataList);
                setListAdapter(eventsAdapter);
                
                if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign()){
                    getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    showEvent(0);
                }    
                
                getListView().setOnScrollListener(EventsFragment.this);
            }else
                eventsAdapter.notifyDataSetChanged();
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