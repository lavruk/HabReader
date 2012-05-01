package net.meiolania.apps.habrahabr.fragments;

public class PastEventFragment extends AbstractionEventsFragment{
    public final static String URL = "http://habrahabr.ru/events/past/page%page%/";
    
    @Override
    protected String getUrl(){
        return URL;
    }

}