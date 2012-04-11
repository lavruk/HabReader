package net.meiolania.apps.habrahabr.fragments;

public class CorporatePostsFragment extends AbstractionPostsFragment{
    public final static String URL = "http://habrahabr.ru/posts/corporative/";
    
    @Override
    protected String getUrl(){
        return URL;
    }

}