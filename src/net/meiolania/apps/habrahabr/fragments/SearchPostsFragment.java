package net.meiolania.apps.habrahabr.fragments;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchPostsFragment extends AbstractionPostsFragment{
    public final static String URL = "http://habrahabr.ru/search/page%page%/?target_type=posts&order_by=relevance&q=%query%";
    protected String query;

    public SearchPostsFragment(String query){
        this.query = query;
    }

    @Override
    protected String getUrl(){
        try{
            return URL.replace("%query%", URLEncoder.encode(query, "UTF-8"));
        }
        catch(UnsupportedEncodingException e){
        }
        return URL.replace("%query%", query);
    }

}