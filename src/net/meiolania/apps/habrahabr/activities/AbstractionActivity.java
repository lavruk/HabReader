package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.Preferences;
import android.content.Context;
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
    }
    
    @Override
    protected void onDestroy(){
        if(wakeLock != null)
            wakeLock.release();
        super.onDestroy();
    }
    
}