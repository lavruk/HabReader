package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;

import net.meiolania.apps.habrahabr.Api;
import net.meiolania.apps.habrahabr.adapters.PostsAdapter;
import net.meiolania.apps.habrahabr.data.PostsData;
import net.meiolania.apps.habrahabr.ui.activities.PostsShowActivity;
import android.content.Intent;
import android.os.AsyncTask;

public class BlogsPostsFragment extends PostsFragment{
    
    @Override
    protected void showPost(int position){
        PostsData postsData = postsDataList.get(position);
        
        Intent intent = new Intent(getActivity(), PostsShowActivity.class);
        intent.putExtra("link", postsData.getLink());

        startActivity(intent);
    }
    
    @Override
    protected void loadList(){
        if(link != null && link.length() > 0){
            ++page;
            new LoadPostsList().execute();
        }
    }
    
    protected class LoadPostsList extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                postsDataList = new Api(getActivity()).getPostsApi().getPosts(link + "/page" + page + "/");

                // Trying to get posts again. Need to rewrite this code
                if(postsDataList.isEmpty())
                    postsDataList = new Api(getActivity()).getPostsApi().getPosts(link + "/page" + page + "/");
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                postsAdapter = new PostsAdapter(getActivity(), postsDataList);
                setListAdapter(postsAdapter);
            }else
                postsAdapter.notifyDataSetChanged();
        }

    }
    
}