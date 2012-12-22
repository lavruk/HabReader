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

package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.qa.QaHotFragment;
import net.meiolania.apps.habrahabr.fragments.qa.QaInboxFragment;
import net.meiolania.apps.habrahabr.fragments.qa.QaPopularFragment;
import net.meiolania.apps.habrahabr.fragments.qa.QaUnansweredFragment;
import net.meiolania.apps.habrahabr.ui.TabListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.MenuItem;

public class QaActivity extends AbstractionActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		showActionBar();
	}

	private void showActionBar()
	{
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.qa);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Preferences preferences = Preferences.getInstance(this);
		int selectedTab = preferences.getQaDefaultTab();

		/* Inbox tab */
		Tab tab = actionBar.newTab()
						   .setText(R.string.inbox)
						   .setTag("inbox")
						   .setTabListener(new TabListener<QaInboxFragment>(this, "inbox", QaInboxFragment.class));
		actionBar.addTab(tab, (selectedTab == 0 ? true : false));

		/* Hot tab */
		tab = actionBar.newTab()
					   .setText(R.string.hot)
					   .setTag("hot")
					   .setTabListener(new TabListener<QaHotFragment>(this, "hot", QaHotFragment.class));
		actionBar.addTab(tab, (selectedTab == 1 ? true : false));

		/* Popular tab */
		tab = actionBar.newTab()
					   .setText(R.string.popular)
					   .setTag("popular")
					   .setTabListener(new TabListener<QaPopularFragment>(this, "popular", QaPopularFragment.class));
		actionBar.addTab(tab, (selectedTab == 2 ? true : false));

		/* Unanswered tab */
		tab = actionBar.newTab()
					   .setText(R.string.unanswered)
					   .setTag("unanswered")
					   .setTabListener(new TabListener<QaUnansweredFragment>(this, "unanswered", QaUnansweredFragment.class));
		actionBar.addTab(tab, (selectedTab == 3 ? true : false));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				Intent intent = new Intent(this, DashboardActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected OnClickListener getConnectionDialogListener()
	{
		return new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
			}
		};
	}

}