/*
   Copyright (C) 2011 Andrey Zaytsev <a.einsam@gmail.com>
  
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
  
        http://www.apache.org/licenses/LICENSE-2.0
  
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

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