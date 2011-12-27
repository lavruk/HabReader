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

package net.meiolania.apps.habrahabr;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences{
    private boolean vibrate;
    private boolean fullscreen;
    private boolean keepScreen;
    private boolean enableFlashPosts;
    private boolean useCSS;
    private boolean roaming;
    private boolean use3g;
    private boolean cache_posts;
    private String cache_posts_numbers;
    private String default_component;
    private SharedPreferences prefs;

    public Preferences(Context context){
        loadPreferences(context);
    }

    private void loadPreferences(Context context){
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        vibrate = sharedPreferences.getBoolean("vibrate", true);
        fullscreen = sharedPreferences.getBoolean("fullscreen", true);
        keepScreen = sharedPreferences.getBoolean("keep_screen", false);
        enableFlashPosts = sharedPreferences.getBoolean("enable_flash_posts", true);
        useCSS = sharedPreferences.getBoolean("use_css", true);
        roaming = sharedPreferences.getBoolean("roaming", false);
        use3g = sharedPreferences.getBoolean("use_3g", true);
        cache_posts = sharedPreferences.getBoolean("cache_posts", false);
        cache_posts_numbers = sharedPreferences.getString("cache_posts_numbers", "1");
        default_component = sharedPreferences.getString("default_component", "Dashboard");

        prefs = sharedPreferences;
    }

    public boolean isVibrate(){
        return vibrate;
    }

    public boolean isFullscreen(){
        return fullscreen;
    }

    public boolean isKeepScreen(){
        return keepScreen;
    }

    public boolean isEnableFlashPosts(){
        return enableFlashPosts;
    }

    public boolean isUseCSS(){
        return useCSS;
    }

    public boolean isRoaming(){
        return roaming;
    }

    public boolean isUse3g(){
        return use3g;
    }

    public boolean isCachePosts(){
        return cache_posts;
    }

    public int getCachePostsNumbers(){
        return Integer.valueOf(cache_posts_numbers);
    }

    public String getDefaultComponent(){
        return default_component;
    }

    public SharedPreferences getSharedPreferences(){
        return prefs;
    }

}