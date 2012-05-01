package net.meiolania.apps.habrahabr.fragments;

public class SearchPostsFragment extends AbstractionPostsFragment{
    public final static String URL = "http://habrahabr.ru/search/page%d/?target_type=posts&order_by=relevance&q=%query%";
    protected String query;

    public SearchPostsFragment(String query){
        this.query = query;
    }

    @Override
    protected String getUrl(){
        //FIXME: problems with Russian text
        return URL.replace("%query%", query);
    }

}