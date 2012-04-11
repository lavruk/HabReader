package net.meiolania.apps.habrahabr.fragments;

public class BestPostsFragment extends AbstractionPostsFragment{
    public final static String URL = "http://habrahabr.ru/posts/top/daily/";
    
    @Override
    protected String getUrl(){
        return URL;
    }

}