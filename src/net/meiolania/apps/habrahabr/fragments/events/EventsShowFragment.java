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

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.EventFullData;
import net.meiolania.apps.habrahabr.fragments.events.loader.EventShowLoader;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import net.meiolania.apps.habrahabr.utils.IntentUtils;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class EventsShowFragment extends SherlockFragment implements LoaderCallbacks<EventFullData> {
    public final static int LOADER_EVENT = 0;
    public final static String URL_ARGUMENT = "url";
    private String url;
    private EventFullData event;
    private ProgressDialog progressDialog;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	url = getArguments().getString(URL_ARGUMENT);

	setRetainInstance(true);
	setHasOptionsMenu(true);

	if (ConnectionUtils.isConnected(getSherlockActivity()))
	    getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_EVENT, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.events_show_activity, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	super.onCreateOptionsMenu(menu, inflater);

	inflater.inflate(R.menu.events_show_activity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.share:
	    IntentUtils.createShareIntent(getSherlockActivity(), event.getTitle(), url);
	    break;
	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<EventFullData> onCreateLoader(int id, Bundle args) {
	showProgressDialog();

	EventShowLoader loader = new EventShowLoader(getSherlockActivity(), url);
	loader.forceLoad();

	return loader;
    }

    @Override
    public void onLoadFinished(Loader<EventFullData> loader, EventFullData data) {
	SherlockFragmentActivity activity = getSherlockActivity();

	if (activity != null) {
	    TextView title = (TextView) activity.findViewById(R.id.event_title);
	    title.setText(data.getTitle());

	    TextView location = (TextView) activity.findViewById(R.id.event_location);
	    location.setText(data.getLocation());

	    TextView pay = (TextView) activity.findViewById(R.id.event_pay);
	    pay.setText(data.getPay());

	    TextView site = (TextView) activity.findViewById(R.id.event_site);
	    site.setText(data.getSite());

	    TextView description = (TextView) activity.findViewById(R.id.event_description);
	    description.setText(data.getText());
	}

	event = data;

	hideProgressDialog();
    }

    @Override
    public void onLoaderReset(Loader<EventFullData> loader) {

    }

    private void showProgressDialog() {
	progressDialog = new ProgressDialog(getSherlockActivity());
	progressDialog.setMessage(getString(R.string.loading_event));
	progressDialog.setCancelable(true);
	progressDialog.show();
    }

    private void hideProgressDialog() {
	if (progressDialog != null)
	    progressDialog.dismiss();
    }

}