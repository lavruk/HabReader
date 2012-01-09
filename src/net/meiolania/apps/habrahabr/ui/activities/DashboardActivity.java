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

package net.meiolania.apps.habrahabr.ui.activities;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.Blogs;
import net.meiolania.apps.habrahabr.activities.Companies;
import net.meiolania.apps.habrahabr.activities.Events;
import net.meiolania.apps.habrahabr.activities.People;
import net.meiolania.apps.habrahabr.activities.Posts;
import net.meiolania.apps.habrahabr.activities.QA;
import net.meiolania.apps.habrahabr.ui.fragments.DashboardFragment;
import net.meiolania.apps.habrahabr.ui.fragments.PostsFragment;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import net.meiolania.apps.habrahabr.utils.VibrateUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

public class DashboardActivity extends ApplicationFragmentActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        
        Fragment dashboard = new DashboardFragment();
        Fragment posts = new PostsFragment();
        
        fragmentTransaction.replace(R.id.dashboard_fragment, dashboard);
        
        if(UIUtils.isTablet(this))
            fragmentTransaction.replace(R.id.posts_fragment, posts);
        else{
            FrameLayout postsFrameLayout = (FrameLayout)findViewById(R.id.posts_fragment);
            postsFrameLayout.setVisibility(View.GONE);
        }
        
        fragmentTransaction.commit();
    }
    
    public void clickDashboardButton(View view){
        if(preferences.isVibrate())
            VibrateUtils.doVibrate(this);
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
                //TODO: search
                break;
        }
    }
    
}