package net.meiolania.apps.habrahabr.fragments;

public class UnansweredQaFragment extends AbstractionQaFragment{
    public final static String URL = "http://habrahabr.ru/qa/unanswered/page%page%/";
    
    @Override
    public String getUrl(){
        return URL;
    }

}