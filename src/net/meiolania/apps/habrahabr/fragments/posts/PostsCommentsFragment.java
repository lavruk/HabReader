/*
Copyright 2012 Andrey Zaytsev

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

package net.meiolania.apps.habrahabr.fragments.posts;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.CommentsAdapter;
import net.meiolania.apps.habrahabr.data.CommentsData;
import net.meiolania.apps.habrahabr.fragments.posts.loader.PostCommentsLoader;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.actionbarsherlock.app.SherlockListFragment;

public class PostsCommentsFragment extends SherlockListFragment implements LoaderCallbacks<ArrayList<CommentsData>>{
	public final static int LOADER_COMMENTS = 0;
    public final static int MENU_OPEN_COMMENT_IN_BROWSER = 0;
    public final static int MENU_OPEN_AUTHOR_PROFILE = 1;
    protected ArrayList<CommentsData> commentsDatas;
    protected CommentsAdapter commentsAdapter;
    protected String url;
    
    //TODO: move to arguments
    public PostsCommentsFragment(String url){
        this.url = url;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        
        setRetainInstance(true);
        
        if(commentsAdapter == null){
        	commentsDatas = new ArrayList<CommentsData>();
        	commentsAdapter = new CommentsAdapter(getSherlockActivity(), commentsDatas);
        }
        
        getListView().setAdapter(commentsAdapter);
        setListShown(true);
        
        registerForContextMenu(getListView());
        
        getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_COMMENTS, null, this);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        
        menu.add(0, MENU_OPEN_COMMENT_IN_BROWSER, 0, R.string.open_comment_in_browser);
        menu.add(0, MENU_OPEN_AUTHOR_PROFILE, 0, R.string.open_author_profile);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo)item.getMenuInfo();
        CommentsData commentsData = (CommentsData)getListAdapter().getItem(adapterContextMenuInfo.position);
        
        switch(item.getItemId()){
            case MENU_OPEN_COMMENT_IN_BROWSER:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(commentsData.getUrl())));
                break;
            case MENU_OPEN_AUTHOR_PROFILE:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(commentsData.getAuthorUrl())));
                break;
        }
        
        return super.onContextItemSelected(item);
    }

	@Override
	public Loader<ArrayList<CommentsData>> onCreateLoader(int id, Bundle args){
		PostCommentsLoader loader = new PostCommentsLoader(getSherlockActivity(), url);
		loader.forceLoad();
		
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<CommentsData>> loader, ArrayList<CommentsData> data){
		commentsDatas.addAll(data);
		commentsAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<CommentsData>> loader){
		
	}

}