package net.meiolania.apps.habrahabr.fragments;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchQaFragment extends AbstractionQaFragment{
    public final static String URL = "http://habrahabr.ru/search/page%page%/?target_type=qa&order_by=relevance&q=%query%";
    protected String query;

    public SearchQaFragment(String query){
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