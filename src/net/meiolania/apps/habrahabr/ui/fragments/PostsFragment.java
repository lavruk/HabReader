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
import net.meiolania.apps.habrahabr.adapters.PostsAdapter;
import net.meiolania.apps.habrahabr.data.PostsData;
import net.meiolania.apps.habrahabr.ui.activities.PostsShowActivity;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class PostsFragment extends ApplicationListFragment implements OnScrollListener{
    protected final ArrayList<PostsData> postsDataList = new ArrayList<PostsData>();
    protected PostsAdapter postsAdapter;
    protected int page = 0;
    protected String link;
    protected boolean canLoadingData = true;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        
        getListView().setOnScrollListener(this);
        
        loadList();

        if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign())
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    public void setLink(String link){
        this.link = link;
    }

    public String getLink(){
        return link;
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        showPost(position);
    }

    protected void showPost(int position){
        PostsData postsData = postsDataList.get(position);

        if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign()){
            getListView().setItemChecked(position, true);

            PostsShowFragment postsShowFragment = (PostsShowFragment) getFragmentManager().findFragmentById(R.id.post_show_fragment);

            if(postsShowFragment == null || postsShowFragment.getLink() != postsData.getLink()){
                postsShowFragment = new PostsShowFragment();
                postsShowFragment.setLink(postsData.getLink());

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.post_show_fragment, postsShowFragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }
        }else{
            Intent intent = new Intent(getActivity(), PostsShowActivity.class);
            intent.putExtra("link", postsData.getLink());

            startActivity(intent);
        }
    }

    protected void loadList(){
        ++page;
        new LoadPostsList().execute();
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
                
                if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign())
                    showPost(0);
            }else
                postsAdapter.notifyDataSetChanged();
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