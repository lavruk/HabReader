package net.meiolania.apps.habrahabr.fragments;

public class ThematicPostsFragment extends AbstractionPostsFragment{
    public final static String URL = "http://habrahabr.ru/posts/collective/page%page%";
    
    @Override
    protected String getUrl(){
        return URL;
    }

}