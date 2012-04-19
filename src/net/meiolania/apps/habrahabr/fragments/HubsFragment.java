package net.meiolania.apps.habrahabr.fragments;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.data.HubsData;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListFragment;

public class HubsFragment extends SherlockListFragment{
    public final static String LOG_TAG = "HubsFragment";
    protected final ArrayList<HubsData> hubsDatas = new ArrayList<HubsData>();
    protected int page = 0;
    protected boolean loadMoreData = true;
    protected String url;
    
    public HubsFragment(){}
    
    public HubsFragment(String url){
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
    }
    
}