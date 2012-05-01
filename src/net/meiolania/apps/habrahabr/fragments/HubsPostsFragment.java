package net.meiolania.apps.habrahabr.fragments;

public class HubsPostsFragment extends AbstractionPostsFragment{
    protected String url;
    
    public HubsPostsFragment(){}
    
    public HubsPostsFragment(String url){
        this.url = url;
    }
    
    public void setUrl(String url){
        this.url = url;
    }
    
    @Override
    protected String getUrl(){
        return url + "posts/page%page%/";
    }
    
}