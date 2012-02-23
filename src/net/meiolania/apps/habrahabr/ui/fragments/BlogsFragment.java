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

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.BlogsAdapter;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.data.BlogsData;
import net.meiolania.apps.habrahabr.ui.activities.PostsActivity;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class BlogsFragment extends ApplicationListFragment implements OnScrollListener{
    protected final ArrayList<BlogsData> blogsDataList = new ArrayList<BlogsData>();
    protected BlogsAdapter blogsAdapter;
    protected int page = 0;
    protected boolean canLoadingData = true;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        loadList();
    }

    private void loadList(){
        if(ConnectionApi.isConnection(getActivity())){
            ++page;
            new LoadBlogsList().execute();
        }
    }

    private void showBlog(int position){
        BlogsData blogsData = blogsDataList.get(position);

        if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign()){
            getListView().setItemChecked(position, true);

            BlogsPostsFragment blogsPostsFragment = (BlogsPostsFragment) getFragmentManager().findFragmentById(R.id.posts_list_fragment);

            if(blogsPostsFragment == null || blogsPostsFragment.getLink() != blogsData.getLink()){
                blogsPostsFragment = new BlogsPostsFragment();
                blogsPostsFragment.setLink(blogsData.getLink());

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.posts_list_fragment, blogsPostsFragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }
        }else{
            Intent intent = new Intent(getActivity(), PostsActivity.class);
            intent.putExtra("link", blogsData.getLink());

            startActivity(intent);
        }
    }

    private class LoadBlogsList extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                getApi().getBlogsApi().getBlogs(blogsDataList, page);
            }
            catch(IOException e){
                
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                blogsAdapter = new BlogsAdapter(getActivity(), blogsDataList);
                setListAdapter(blogsAdapter);
                
                if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign()){
                    getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    showBlog(0);
                }    
                
                getListView().setOnScrollListener(BlogsFragment.this);
            }else
                blogsAdapter.notifyDataSetChanged();
            canLoadingData = true;
        }

    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        showBlog(position);
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if((firstVisibleItem + visibleItemCount) == totalItemCount && page != 0 && canLoadingData){
            canLoadingData = false;
            loadList();
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState){}

}