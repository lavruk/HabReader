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
import net.meiolania.apps.habrahabr.fragments.qa.HotQaFragment;
import net.meiolania.apps.habrahabr.fragments.qa.InboxQaFragment;
import net.meiolania.apps.habrahabr.fragments.qa.PopularQaFragment;
import net.meiolania.apps.habrahabr.fragments.qa.UnansweredQaFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.MenuItem;

public class QaActivity extends AbstractionActivity implements TabListener{
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        showActionBar();
    }
    
    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.qa);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        Preferences preferences = Preferences.getInstance(this);
        int selectedTab = preferences.getQaDefaultTab();
        
        Tab tab = actionBar.newTab().setText(R.string.inbox).setTag("inbox").setTabListener(this);
        actionBar.addTab(tab, (selectedTab == 0 ? true : false));
        
        tab = actionBar.newTab().setText(R.string.hot).setTag("hot").setTabListener(this);
        actionBar.addTab(tab, (selectedTab == 1 ? true : false));
        
        tab = actionBar.newTab().setText(R.string.popular).setTag("popular").setTabListener(this);
        actionBar.addTab(tab, (selectedTab == 2 ? true : false));
        
        tab = actionBar.newTab().setText(R.string.unanswered).setTag("unanswered").setTabListener(this);
        actionBar.addTab(tab, (selectedTab == 3 ? true : false));
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft){
        if(tab.getTag().equals("inbox")){
            InboxQaFragment inboxQaFragment = new InboxQaFragment();
            ft.replace(android.R.id.content, inboxQaFragment);
        }else if(tab.getTag().equals("hot")){
            HotQaFragment hotQaFragment = new HotQaFragment();
            ft.replace(android.R.id.content, hotQaFragment);
        }else if(tab.getTag().equals("popular")){
            PopularQaFragment popularQaFragment = new PopularQaFragment();
            ft.replace(android.R.id.content, popularQaFragment);
        }else if(tab.getTag().equals("unanswered")){
            UnansweredQaFragment unansweredQaFragment = new UnansweredQaFragment();
            ft.replace(android.R.id.content, unansweredQaFragment);
        }
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft){
        
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft){
        
    }
    
}