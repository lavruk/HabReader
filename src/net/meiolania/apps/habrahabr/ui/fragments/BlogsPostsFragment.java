package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;

import net.meiolania.apps.habrahabr.adapters.PostsAdapter;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.data.PostsData;
import net.meiolania.apps.habrahabr.ui.activities.PostsShowActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.AbsListView.OnScrollListener;

public class BlogsPostsFragment extends PostsFragment implements OnScrollListener{
    
    @Override
    protected void showPost(int position){
        PostsData postsData = postsDataList.get(position);
        
        Intent intent = new Intent(getActivity(), PostsShowActivity.class);
        intent.putExtra("link", postsData.getLink());

        startActivity(intent);
    }
    
    @Override
    protected void loadList(){
        if(link != null && !link.isEmpty() && ConnectionApi.isConnection(getActivity())){
            ++page;
            new LoadPostsList().execute();
        }
    }
    
    protected class LoadPostsList extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                getApi().getPostsApi().getPosts(postsDataList, link + "/page" + page + "/");

                // Trying to get posts again. Need to rewrite this code
                if(postsDataList.isEmpty())
                    getApi().getPostsApi().getPosts(postsDataList, link + "/page" + page + "/");
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
            canLoadingData = true;
        }

    }
    
}