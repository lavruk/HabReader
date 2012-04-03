package net.meiolania.apps.habrahabr.activities.fragments;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class AbstractPostsFragment extends SherlockFragment{
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        
    }
    
    public abstract String getSectionUrl();
    
}