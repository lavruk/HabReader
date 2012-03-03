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

package net.meiolania.apps.habrahabr.ui.dashboard;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.actions.HomeAction;
import net.meiolania.apps.habrahabr.ui.activities.ApplicationFragmentActivity;
import net.meiolania.apps.habrahabr.ui.blogs.BlogsActivity;
import net.meiolania.apps.habrahabr.ui.companies.CompaniesActivity;
import net.meiolania.apps.habrahabr.ui.events.EventsActivity;
import net.meiolania.apps.habrahabr.ui.people.PeopleActivity;
import net.meiolania.apps.habrahabr.ui.posts.PostsActivity;
import net.meiolania.apps.habrahabr.ui.qa.QaActivity;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import net.meiolania.apps.habrahabr.utils.VibrateUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.markupartist.android.widget.ActionBar;

public class DashboardActivity extends ApplicationFragmentActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.dashboard_activity);
        
        setActionBar();
        
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        
        DashboardFragment dashboard = new DashboardFragment();
        
        fragmentTransaction.replace(R.id.dashboard_fragment, dashboard);
        
        if(UIUtils.isTablet(this) || preferences.isUseTabletDesign()){
            PostsDashboardFragment posts = new PostsDashboardFragment();
            fragmentTransaction.replace(R.id.posts_fragment, posts);
        }else{
            FrameLayout postsFrameLayout = (FrameLayout)findViewById(R.id.posts_fragment);
            postsFrameLayout.setVisibility(View.GONE);
        }
        
        fragmentTransaction.commit();
    }
    
    private void setActionBar(){
        if(!UIUtils.isHoneycombOrHigher()){
            ActionBar actionBar = (ActionBar)findViewById(R.id.actionbar);
            
            if(actionBar != null){
                actionBar.setTitle(R.string.app_name);
                actionBar.setHomeAction(new HomeAction(this, preferences, true));
            }
        }else{
            ActionBar actionBarView = (ActionBar) findViewById(R.id.actionbar);
            
            if(actionBarView != null)
                actionBarView.setVisibility(View.GONE);
            
            android.app.ActionBar actionBar = getActionBar();
            actionBar.setTitle(R.string.app_name);
            
            if(UIUtils.isIceCreamOrHigher())
                actionBar.setHomeButtonEnabled(false);
        }
    }
    
    @Override
    protected Intent getActionBarIntent(){
        return null;
    }
    
    public void clickDashboardButton(View view){
        if(preferences.isVibrate())
            VibrateUtils.doVibrate(this);
        switch(view.getId()){
            case R.id.posts:
                startActivity(new Intent(this, PostsActivity.class));
                break;
            case R.id.blogs:
                startActivity(new Intent(this, BlogsActivity.class));
                break;
            case R.id.qa:
                startActivity(new Intent(this, QaActivity.class));
                break;
            case R.id.events:
                startActivity(new Intent(this, EventsActivity.class));
                break;
            case R.id.companies:
                startActivity(new Intent(this, CompaniesActivity.class));
                break;
            case R.id.people:
                startActivity(new Intent(this, PeopleActivity.class));
                break;
        }
    }
    
}