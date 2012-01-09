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

import net.meiolania.apps.habrahabr.Api;
import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import net.meiolania.apps.habrahabr.utils.VibrateUtils;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.WindowManager;

public abstract class ApplicationFragmentActivity extends FragmentActivity{
    protected PowerManager.WakeLock powerManagerWakeLock;
    protected Preferences preferences;
    protected SharedPreferences sharedPreferences;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        preferences = new Preferences(this);
        sharedPreferences = preferences.getSharedPreferences();
        
        if(preferences.isFullscreen())
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(preferences.isKeepScreen()){
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            powerManagerWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        }
        
        if(UIUtils.isHoneycombOrHigher())
            setTheme(android.R.style.Theme_Holo_Light);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(preferences.isVibrate())
            VibrateUtils.doVibrate(this);
        if(UIUtils.isHoneycombOrHigher()){
            switch(item.getItemId()){
                case android.R.id.home:
                    Intent intent = new Intent(this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
            }
        }
        switch(item.getItemId()){
            case R.id.to_home:
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        
    }

    @Override
    protected void onResume(){
        super.onResume();
        preferences = new Preferences(this);
        sharedPreferences = preferences.getSharedPreferences();

        if(preferences.isFullscreen())
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(preferences.isKeepScreen()){
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            powerManagerWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        if(preferences.isKeepScreen())
            powerManagerWakeLock.release();
    }

    protected Api getApi(){
        Api api = new Api(this);
        return api;
    }   
    
}