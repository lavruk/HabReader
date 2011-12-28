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

package net.meiolania.apps.habrahabr.activities;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.Connection;
import net.meiolania.apps.habrahabr.pager.SimplePagerAdapter;
import net.meiolania.apps.habrahabr.utils.Vibrate;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Dashboard extends ApplicationActivity{
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        showTextForUpdates();
        checkMobileInternetPreferences();
        addScreens();
    }

    private void showTextForUpdates(){

    }

    private void addScreens(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        ArrayList<View> pages = new ArrayList<View>();

        View page = layoutInflater.inflate(R.layout.dashboard_page1, null);
        pages.add(page);

        page = layoutInflater.inflate(R.layout.dashboard_page2, null);
        pages.add(page);

        SimplePagerAdapter pagerAdapter = new SimplePagerAdapter(pages);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
    }

    private void checkMobileInternetPreferences(){
        if(!preferences.isUse3g()){
            if(Connection.isMobileNetwork(this)){
                if(!preferences.isRoaming() && Connection.isRoaming(this)){
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle(R.string.attention);
                    alertDialog.setMessage(getString(R.string.attention_roaming));
                    alertDialog.setButton(getString(R.string.continue_anyway), new OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setButton2(getString(R.string.cancel), new OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            finish();
                        }
                    });
                    alertDialog.setCancelable(true);
                    alertDialog.show();
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle(R.string.attention);
                    alertDialog.setMessage(getString(R.string.attention_use_3g));
                    alertDialog.setButton(getString(R.string.continue_anyway), new OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setButton2(getString(R.string.cancel), new OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            finish();
                        }
                    });
                    alertDialog.setCancelable(true);
                    alertDialog.show();
                }
            }
        }
    }

    public void clickDashboardButton(View view){
        if(preferences.isVibrate())
            Vibrate.doVibrate(this);
        switch(view.getId()){
            case R.id.posts:
                startActivity(new Intent(this, Posts.class));
                break;
            case R.id.blogs:
                startActivity(new Intent(this, Blogs.class));
                break;
            case R.id.qa:
                startActivity(new Intent(this, QA.class));
                break;
            case R.id.events:
                startActivity(new Intent(this, Events.class));
                break;
            case R.id.companies:
                startActivity(new Intent(this, Companies.class));
                break;
            case R.id.people:
                startActivity(new Intent(this, People.class));
                break;
            case R.id.search:
                searchPosts();
                break;
        }
    }
    
    private void searchPosts(){
        EditText searchField = (EditText) findViewById(R.id.search_text);
        if(!searchField.getText().equals("")){
            
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(preferences.isVibrate())
            Vibrate.doVibrate(this);
        switch(item.getItemId()){
            case R.id.about:
                startActivity(new Intent(this, net.meiolania.apps.habrahabr.activities.dashboard.About.class));
                break;
            case R.id.preferences:
                startActivity(new Intent(this, net.meiolania.apps.habrahabr.activities.dashboard.Preferences.class));
                break;
            case R.id.search:
                viewPager.setCurrentItem(1);
                EditText searchField = (EditText) findViewById(R.id.search_text);
                searchField.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(inputMethodManager.SHOW_FORCED, 0);
                break;
            case R.id.other_applications:
                Uri uri = Uri.parse("https://market.android.com/developer?pub=Meiolania.net");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
        return true;
    }

}