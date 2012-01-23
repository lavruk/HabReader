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
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.adapters.PostsAdapter;
import net.meiolania.apps.habrahabr.data.PostsData;
import net.meiolania.apps.habrahabr.ui.activities.PostsShowActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class PostsDashboardFragment extends ApplicationListFragment implements OnScrollListener{
    protected final ArrayList<PostsData> postsDataList = new ArrayList<PostsData>();
    protected PostsAdapter postsAdapter;
    protected int page = 0;
    protected boolean canLoadingData = true;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        
        getListView().setOnScrollListener(this);
        
        loadList();
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        PostsData postsData = postsDataList.get(position);

        Intent intent = new Intent(getActivity(), PostsShowActivity.class);
        intent.putExtra("link", postsData.getLink());

        startActivity(intent);
    }

    private void loadList(){
        ++page;
        new LoadPostsList().execute();
    }

    private class LoadPostsList extends AsyncTask<Void, Void, ArrayList<PostsData>>{

        @Override
        protected ArrayList<PostsData> doInBackground(Void... params){
            try{
                getApi().getPostsApi().getPosts(postsDataList, "http://habrahabr.ru/blogs/page" + page + "/");

                if(postsDataList.isEmpty())
                    getApi().getPostsApi().getPosts(postsDataList, "http://habrahabr.ru/blogs/page" + page + "/");
            
                return postsDataList;
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<PostsData> result){
            if(result != null){
                if(!isCancelled() && page == 1){
                    postsAdapter = new PostsAdapter(getActivity(), result);
                    setListAdapter(postsAdapter);
                }else
                    postsAdapter.notifyDataSetChanged();
            }
            canLoadingData = true;
        }

    }
    
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if((firstVisibleItem + visibleItemCount) == totalItemCount && page != 0 && canLoadingData){
            canLoadingData = false;
            loadList();
        }    
    }

    public void onScrollStateChanged(AbsListView view, int scrollState){}

}