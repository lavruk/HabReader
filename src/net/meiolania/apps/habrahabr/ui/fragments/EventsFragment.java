package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.EventsAdapter;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.data.EventsData;
import net.meiolania.apps.habrahabr.ui.activities.EventsShowActivity;
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
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        
        getListView().setOnScrollListener(this);
        
        loadList();

        if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign())
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
                
                if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign())
                    showEvent(0);
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