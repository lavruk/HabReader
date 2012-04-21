package net.meiolania.apps.habrahabr.fragments;

public class PopularQaFragment extends AbstractionQaFragment{
    public final static String URL = "http://habrahabr.ru/qa/popular/";

    @Override
    public String getUrl(){
        return URL;
    }

}