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
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public abstract class AbstractionActivity extends SherlockFragmentActivity{
    protected Preferences preferences;
    protected PowerManager.WakeLock wakeLock;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        
        preferences = Preferences.getInstance(this);
        if(preferences.getFullScreen()){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        
        if(preferences.getKeepScreen()){
            final PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "wakeLock");
            wakeLock.acquire();
        }
        
        if(!ConnectionUtils.isConnected(this)){
        	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    		dialog.setTitle(R.string.error);
    		dialog.setMessage(getString(R.string.no_connection));
    		dialog.setPositiveButton(R.string.close, getConnectionDialogListener());
    		dialog.setCancelable(false);
    		dialog.show();
        }
    }
    
    @Override
    protected void onDestroy(){
        if(wakeLock != null)
            wakeLock.release();
        super.onDestroy();
    }
    
    protected abstract OnClickListener getConnectionDialogListener();
    
}