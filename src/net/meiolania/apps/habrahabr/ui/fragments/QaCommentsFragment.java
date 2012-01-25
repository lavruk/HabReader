package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.adapters.CommentsAdapter;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.data.CommentsData;
import android.os.AsyncTask;
import android.os.Bundle;

public class QaCommentsFragment extends ApplicationListFragment{
    protected ArrayList<CommentsData> commentsDataList;
    protected String link;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        
        if(link != null && !link.isEmpty())
            loadComments();
    }

    private void loadComments(){
        if(ConnectionApi.isConnection(getActivity()))
            new LoadComments().execute();
    }

    private class LoadComments extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                commentsDataList = getApi().getQaCommentsApi().getComments(link);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled())
                setListAdapter(new CommentsAdapter(getActivity(), commentsDataList));
        }

    }

    public void setLink(String link){
        this.link = link;
    }

}