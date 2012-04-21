package net.meiolania.apps.habrahabr.fragments;

public class HotQaFragment extends AbstractionQaFragment{
    public final static String URL = "http://habrahabr.ru/qa/hot/";
    
    @Override
    public String getUrl(){
        return URL;
    }

}