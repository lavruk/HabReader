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

package net.meiolania.apps.habrahabr.fragments.events;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.activities.EventsShowActivity;
import net.meiolania.apps.habrahabr.adapters.EventsAdapter;
import net.meiolania.apps.habrahabr.data.EventsData;
import net.meiolania.apps.habrahabr.fragments.events.loader.EventLoader;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public abstract class AbstractionEventsFragment extends SherlockListFragment implements OnScrollListener, LoaderCallbacks<ArrayList<EventsData>>{
	private int page;
	private boolean isLoadData;
	protected ArrayList<EventsData> eventsDatas;
	protected EventsAdapter eventsAdapter;

	protected abstract String getUrl();

	protected abstract int getLoaderId();

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);

		setRetainInstance(true);

		if(eventsAdapter == null){
			eventsDatas = new ArrayList<EventsData>();
			eventsAdapter = new EventsAdapter(getSherlockActivity(), eventsDatas);
		}

		setListAdapter(eventsAdapter);
		setListShown(true);

		getListView().setOnScrollListener(this);
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id){
		showEvent(position);
	}

	protected void showEvent(int position){
		EventsData eventsData = eventsDatas.get(position);

		Intent intent = new Intent(getSherlockActivity(), EventsShowActivity.class);
		intent.putExtra(EventsShowActivity.EXTRA_TITLE, eventsData.getTitle());
		intent.putExtra(EventsShowActivity.EXTRA_URL, eventsData.getUrl());

		startActivity(intent);
	}

	protected void restartLoading(){
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

		EventLoader.setPage(++page);

		getSherlockActivity().getSupportLoaderManager().restartLoader(getLoaderId(), null, this);

		isLoadData = true;
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
		if((firstVisibleItem + visibleItemCount) == totalItemCount && !isLoadData)
			restartLoading();
	}

	public void onScrollStateChanged(AbsListView view, int scrollState){

	}

	@Override
	public Loader<ArrayList<EventsData>> onCreateLoader(int id, Bundle args){
		EventLoader loader = new EventLoader(getSherlockActivity(), getUrl());
		loader.forceLoad();

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<EventsData>> loader, ArrayList<EventsData> data){
		eventsDatas.addAll(data);
		eventsAdapter.notifyDataSetChanged();

		if(getSherlockActivity() != null)
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

		isLoadData = false;
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<EventsData>> loader){

	}

}