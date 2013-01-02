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

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.hubs.HubsFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;

public class HubsActivity extends AbstractionActivity implements OnNavigationListener {
    public final static int LIST_ALL_HUBS = 0;
    public final static int LIST_API = 1;
    public final static int LIST_ADMIN = 2;
    public final static int LIST_DB = 3;
    public final static int LIST_SECURITY = 4;
    public final static int LIST_DESIGN = 5;
    public final static int LIST_GADGETS = 6;
    public final static int LIST_COMPANIES = 7;
    public final static int LIST_MANAGEMENT = 8;
    public final static int LIST_PROGRAMMING = 9;
    public final static int LIST_SOFTWARE = 10;
    public final static int LIST_TELECOMMUNICATIONS = 11;
    public final static int LIST_FRAMEWORKS = 12;
    public final static int LIST_FRONTEND = 13;
    public final static int LIST_OTHERS = 14;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	showActionBar();
    }

    private void showActionBar() {
	ActionBar actionBar = getSupportActionBar();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

	ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(getSupportActionBar().getThemedContext(), R.array.hubs_list,
		R.layout.sherlock_spinner_item);
	list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
	actionBar.setListNavigationCallbacks(list, this);
    }

    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
	HubsFragment fragment = new HubsFragment();
	FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

	Bundle arguments = new Bundle();
	switch ((int) itemId) {
	default:
	case LIST_ALL_HUBS:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/page%page%/");
	    break;
	case LIST_API:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/api/page%page%/");
	    break;
	case LIST_ADMIN:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/administration/page%page%/");
	    break;
	case LIST_DB:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/databases/page%page%/");
	    break;
	case LIST_SECURITY:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/security/page%page%/");
	    break;
	case LIST_DESIGN:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/design-and-media/page%page%/");
	    break;
	case LIST_GADGETS:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/hardware/page%page%/");
	    break;
	case LIST_COMPANIES:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/companies-and-services/page%page%/");
	    break;
	case LIST_MANAGEMENT:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/management/page%page%/");
	    break;
	case LIST_PROGRAMMING:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/programming/page%page%/");
	    break;
	case LIST_SOFTWARE:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/software/page%page%/");
	    break;
	case LIST_TELECOMMUNICATIONS:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/telecommunications/page%page%/");
	    break;
	case LIST_FRAMEWORKS:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/fw-and-cms/page%page%/");
	    break;
	case LIST_FRONTEND:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/frontend/page%page%/");
	    break;
	case LIST_OTHERS:
	    arguments.putString(HubsFragment.URL_ARGUMENT, "http://habrahabr.ru/hubs/others/page%page%/");
	    break;
	}

	fragment.setArguments(arguments);

	fragmentTransaction.replace(android.R.id.content, fragment);
	fragmentTransaction.commit();
	return false;
    }

    @Override
    protected OnClickListener getConnectionDialogListener() {
	return new OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		finish();
	    }
	};
    }

}