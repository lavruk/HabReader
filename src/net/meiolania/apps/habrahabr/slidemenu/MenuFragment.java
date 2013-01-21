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

package net.meiolania.apps.habrahabr.slidemenu;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.AuthActivity;
import net.meiolania.apps.habrahabr.activities.CompaniesActivity;
import net.meiolania.apps.habrahabr.activities.EventsActivity;
import net.meiolania.apps.habrahabr.activities.HubsActivity;
import net.meiolania.apps.habrahabr.activities.PostsActivity;
import net.meiolania.apps.habrahabr.activities.QaActivity;
import net.meiolania.apps.habrahabr.activities.SignOutActivity;
import net.meiolania.apps.habrahabr.activities.UsersActivity;
import net.meiolania.apps.habrahabr.auth.User;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class MenuFragment extends SherlockListFragment {
    private ArrayList<MenuData> menu;
    private MenuAdapter menuAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	menu = new ArrayList<MenuData>();
	
	// Auth
	if(!User.getInstance().isLogin())
	    menu.add(new MenuData(getString(R.string.auth), R.drawable.ic_menu_user, AuthActivity.class));
	else{
	    menu.add(new MenuData(User.getInstance().getLogin(), R.drawable.ic_menu_user, null));
	    menu.add(new MenuData(getString(R.string.sign_out), R.drawable.ic_menu_user, SignOutActivity.class));
	}
	
	menu.add(new MenuData(getString(R.string.posts), R.drawable.ic_menu_posts, PostsActivity.class));
	menu.add(new MenuData(getString(R.string.hubs), R.drawable.ic_menu_hubs, HubsActivity.class));
	menu.add(new MenuData(getString(R.string.qa), R.drawable.ic_menu_qa, QaActivity.class));
	menu.add(new MenuData(getString(R.string.events), R.drawable.ic_menu_events, EventsActivity.class));
	menu.add(new MenuData(getString(R.string.companies), R.drawable.ic_menu_companies, CompaniesActivity.class));
	menu.add(new MenuData(getString(R.string.people), R.drawable.ic_menu_user, UsersActivity.class));

	menuAdapter = new MenuAdapter(getSherlockActivity(), menu);
	setListAdapter(menuAdapter);
	setListShown(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
	MenuData data = menu.get(position);

	Intent intent = new Intent(getSherlockActivity(), data.cls);
	startActivity(intent);
    }

    private class MenuData {
	public String title;
	public int icon;
	public Class<?> cls;

	public MenuData(String title, int icon, Class<?> cls) {
	    this.title = title;
	    this.icon = icon;
	    this.cls = cls;
	}

    }

    private class MenuAdapter extends BaseAdapter {
	private ArrayList<MenuData> data;
	private Context context;

	public MenuAdapter(Context context, ArrayList<MenuData> data) {
	    this.data = data;
	    this.context = context;
	}

	@Override
	public int getCount() {
	    return data.size();
	}

	@Override
	public MenuData getItem(int position) {
	    return data.get(position);
	}

	@Override
	public long getItemId(int position) {
	    return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    MenuData data = getItem(position);

	    View view = convertView;
	    if (view == null) {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.slide_menu_row, null);
	    }

	    TextView title = (TextView) view.findViewById(R.id.slide_menu_title);
	    ImageView icon = (ImageView) view.findViewById(R.id.slide_menu_icon);

	    title.setText(data.title);
	    icon.setImageResource(data.icon);

	    return view;
	}

    }

}