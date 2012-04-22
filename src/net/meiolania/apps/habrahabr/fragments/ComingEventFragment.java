package net.meiolania.apps.habrahabr.fragments;

public class ComingEventFragment extends AbstractionEventsFragment{
    public final static String URL = "http://habrahabr.ru/events/coming/";
    
    @Override
    protected String getUrl(){
        return URL;
    }
    
}