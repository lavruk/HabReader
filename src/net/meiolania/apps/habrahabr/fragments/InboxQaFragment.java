package net.meiolania.apps.habrahabr.fragments;

public class InboxQaFragment extends AbstractionQaFragment{
    public final static String URL = "http://habrahabr.ru/qa/page%page%/";
    
    @Override
    public String getUrl(){
        return URL;
    }

}