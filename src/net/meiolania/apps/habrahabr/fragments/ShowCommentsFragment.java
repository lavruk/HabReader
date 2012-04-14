package net.meiolania.apps.habrahabr.fragments;

import com.actionbarsherlock.app.SherlockListFragment;

public class ShowCommentsFragment extends SherlockListFragment{
    protected String url;
    
    public ShowCommentsFragment(){}
    
    public ShowCommentsFragment(String url){
        this.url = url;
    }
    
    public void setUrl(String url){
        this.url = url;
    }
    
    public String getUrl(){
        return url;
    }
    
}