package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.adapters.CommentsAdapter;
import net.meiolania.apps.habrahabr.data.CommentsData;
import android.os.AsyncTask;
import android.os.Bundle;

public class PostsCommentsFragment extends ApplicationListFragment{
    private ArrayList<CommentsData> commentsDataList;
    private String link;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        
        loadComments();
    }
    
    private void loadComments(){
        new LoadComments().execute();
    }
    
    public void setLink(String link){
        this.link = link;
    }
    
    private class LoadComments extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                commentsDataList = getApi().getPostsCommentsApi().getComments(link);
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
    
}