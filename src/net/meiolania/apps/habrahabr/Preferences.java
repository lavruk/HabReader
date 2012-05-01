package net.meiolania.apps.habrahabr;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class Preferences{
    /* Default tab for posts */
    public final static String POSTS_DEFAULT_TAB_KEY = "posts_default_tab";
    public final static String POSTS_DEFAULT_TAB_DEFAULT = "1";
    /* Default tab for q&a */
    public final static String QA_DEFAULT_TAB_KEY = "qa_default_tab";
    public final static String QA_DEFAULT_TAB_DEFAULT = "0";
    /* Default tab for events */
    public final static String EVENTS_DEFAULT_TAB_KEY = "events_default_tab";
    public final static String EVENTS_DEFAULT_TAB_DEFAULT = "0";
    /* Fullscreen */
    public final static String FULLSCREEN_KEY = "fullscreen";
    public final static boolean FULLSCREEN_DEFAULT = false;
    /* Keepscreen */
    public final static String KEEPSCREEN_KEY = "keepscreen";
    public final static boolean KEEPSCREEN_DEFAULT = false;
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
    
    public boolean getKeepScreen(){
        return sharedPreferences.getBoolean(KEEPSCREEN_KEY, KEEPSCREEN_DEFAULT);
    }
    
    public int getPostsDefaultTab(){
        return Integer.parseInt(sharedPreferences.getString(POSTS_DEFAULT_TAB_KEY, POSTS_DEFAULT_TAB_DEFAULT));
    }
    
    public int getEventsDefaultTab(){
        return Integer.parseInt(sharedPreferences.getString(EVENTS_DEFAULT_TAB_KEY, EVENTS_DEFAULT_TAB_DEFAULT));
    }
    
    public int getQaDefaultTab(){
        return Integer.parseInt(sharedPreferences.getString(QA_DEFAULT_TAB_KEY, QA_DEFAULT_TAB_DEFAULT));
    }
    
}