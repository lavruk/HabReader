package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.PeopleAdapter;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.data.PeopleData;
import net.meiolania.apps.habrahabr.ui.activities.PeopleShowActivity;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class PeopleFragment extends ApplicationListFragment implements OnScrollListener{
    protected final ArrayList<PeopleData> peopleDataList = new ArrayList<PeopleData>();
    protected PeopleAdapter peopleAdapter;
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
        showUser(position);
    }

    protected void showUser(int position){
        PeopleData peopleData = peopleDataList.get(position);

        if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign()){
            getListView().setItemChecked(position, true);

            PeopleShowFragment peopleShowFragment = (PeopleShowFragment) getFragmentManager().findFragmentById(R.id.people_show_fragment);

            if(peopleShowFragment == null || !peopleShowFragment.getLink().equals(peopleData.getLink())){
                peopleShowFragment = new PeopleShowFragment();
                peopleShowFragment.setLink(peopleData.getLink());

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.people_show_fragment, peopleShowFragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }
        }else{
            Intent intent = new Intent(getActivity(), PeopleShowActivity.class);
            intent.putExtra("link", peopleData.getLink());

            startActivity(intent);
        }
    }

    protected void loadList(){
        if(ConnectionApi.isConnection(getActivity())){
            ++page;
            new LoadPeopleList().execute();
        }
    }

    private class LoadPeopleList extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                getApi().getPeopleApi().getPeople(peopleDataList, page);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                peopleAdapter = new PeopleAdapter(getActivity(), peopleDataList);
                setListAdapter(peopleAdapter);
                
                if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign())
                    showUser(0);
            }else
                peopleAdapter.notifyDataSetChanged();
            canLoadingData = true;
        }

    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if((firstVisibleItem + visibleItemCount) == totalItemCount && page != 0 && canLoadingData){
            canLoadingData = false;
            loadList();
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState){
    }

}