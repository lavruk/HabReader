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
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

public abstract class ApplicationActivity extends Activity{
    protected PowerManager.WakeLock powerManagerWakeLock;
    protected SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        sharedPreferences = Preferences.loadPreferences(this);

        if(Preferences.fullscreen)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(Preferences.keepScreen){
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            powerManagerWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Preferences.loadPreferences(this);
        
        if(Preferences.keepScreen){
            if(powerManagerWakeLock == null){
                PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                powerManagerWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
            }
            powerManagerWakeLock.acquire();
        }    
    }

    @Override
    protected void onPause(){
        super.onPause();
        
        if(Preferences.keepScreen)
            powerManagerWakeLock.release();
    }

}