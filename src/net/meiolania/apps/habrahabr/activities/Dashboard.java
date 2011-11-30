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

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.utils.Vibrate;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class Dashboard extends ApplicationActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_SEARCH:
                startActivity(new Intent(this, PostsSearch.class));
                break;
        }
        /*
         * FIXME: volume buttons don`t work
         */
        return super.onKeyDown(keyCode, event);
    }

    public void clickDashboardButton(View view){
        if(Preferences.vibrate)
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
        if(Preferences.vibrate)
            Vibrate.doVibrate(this);
        switch(item.getItemId()){
            case R.id.about:
                startActivity(new Intent(this, net.meiolania.apps.habrahabr.activities.dashboard.About.class));
                break;
            case R.id.preferences:
                startActivity(new Intent(this, net.meiolania.apps.habrahabr.activities.dashboard.Preferences.class));
                break;
        }
        return true;
    }

}