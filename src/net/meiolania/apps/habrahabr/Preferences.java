package net.meiolania.apps.habrahabr;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class Preferences{
    /* Fullscreen */
    public final static String FULLSCREEN_KEY = "fullscreen";
    public final static boolean FULLSCREEN_DEFAULT = false;
    private static Preferences preferences = null;
    private SharedPreferences sharedPreferences;
    
    private Preferences(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public static Preferences getInstance(Context context){
        return preferences != null ? preferences : (preferences = new Preferences(context));
    }
    
    public boolean getFullScreen(){
        return sharedPreferences.getBoolean(FULLSCREEN_KEY, FULLSCREEN_DEFAULT);
    }
    
}