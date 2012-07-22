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

package net.meiolania.apps.habrahabr.fragments.people;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.PeopleSearchActivity;
import net.meiolania.apps.habrahabr.activities.PeopleShowActivity;
import net.meiolania.apps.habrahabr.adapters.PeopleAdapter;
import net.meiolania.apps.habrahabr.data.PeopleData;
import net.meiolania.apps.habrahabr.fragments.people.loader.PeopleLoader;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class PeopleFragment extends SherlockListFragment implements OnScrollListener, LoaderCallbacks<ArrayList<PeopleData>>{
	public final static String URL_ARGUMENT = "url";
	public final static int LOADER_PEOPLE = 0;
	private ArrayList<PeopleData> people;
	private PeopleAdapter adapter;
	private int page;
	private String url;
	private boolean isLoadData;

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);

		if(getArguments() != null)
			url = getArguments().getString(URL_ARGUMENT);

		setRetainInstance(true);
		setHasOptionsMenu(true);

		if(adapter == null){
			people = new ArrayList<PeopleData>();
			adapter = new PeopleAdapter(getSherlockActivity(), people);
		}

		setListAdapter(adapter);
		setListShown(true);

		getListView().setOnScrollListener(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.people_fragment, menu);

		final EditText searchQuery = (EditText) menu.findItem(R.id.search).getActionView().findViewById(R.id.search_query);
		searchQuery.setOnEditorActionListener(new OnEditorActionListener(){
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
				if(actionId == EditorInfo.IME_ACTION_SEARCH){
					Intent intent = new Intent(getSherlockActivity(), PeopleSearchActivity.class);
					intent.putExtra(PeopleSearchActivity.EXTRA_QUERY, searchQuery.getText().toString());
					startActivity(intent);
					return true;
				}
				return false;
			}
		});

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id){
		showUser(position);
	}

	protected void showUser(int position){
		PeopleData data = people.get(position);

		Intent intent = new Intent(getSherlockActivity(), PeopleShowActivity.class);
		intent.putExtra(PeopleShowActivity.EXTRA_NAME, data.getName());
		intent.putExtra(PeopleShowActivity.EXTRA_URL, data.getUrl());

		startActivity(intent);
	}

	protected void restartLoading(){
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

		PeopleLoader.setPage(++page);

		getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_PEOPLE, null, this);

		isLoadData = true;
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
		if((firstVisibleItem + visibleItemCount) == totalItemCount && !isLoadData)
			restartLoading();
	}

	public void onScrollStateChanged(AbsListView view, int scrollState){

	}

	@Override
	public Loader<ArrayList<PeopleData>> onCreateLoader(int id, Bundle args){
		PeopleLoader loader = null;

		if(url == null)
			loader = new PeopleLoader(getSherlockActivity());
		else
			loader = new PeopleLoader(getSherlockActivity(), url);

		loader.forceLoad();

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<PeopleData>> loader, ArrayList<PeopleData> data){
		people.addAll(data);
		adapter.notifyDataSetChanged();

		if(getSherlockActivity() != null)
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

		isLoadData = false;
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<PeopleData>> loader){

	}

}