package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.BlogsAdapter;
import net.meiolania.apps.habrahabr.data.BlogsData;
import net.meiolania.apps.habrahabr.ui.activities.PostsActivity;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;

public class BlogsFragment extends ApplicationListFragment{
    private ArrayList<BlogsData> blogsDataList;
    private BlogsAdapter blogsAdapter;
    private int page;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        loadList();
    }

    private void loadList(){
        ++page;
        new LoadBlogsList().execute();
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
                blogsDataList = getApi().getBlogsApi().getBlogs(page);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                blogsAdapter = new BlogsAdapter(getActivity(), blogsDataList);
                setListAdapter(blogsAdapter);
                
                if(UIUtils.isTablet(getActivity()) || preferences.isUseTabletDesign())
                    showBlog(0);
            }else
                blogsAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        showBlog(position);
    }

}