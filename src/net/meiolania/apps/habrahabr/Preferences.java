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
    public static boolean vibrate;
    public static boolean fullscreen;
    public static boolean keepScreen;
    public static boolean enableFlashPosts;
    public static boolean useCSS;

    public static SharedPreferences loadPreferences(Context context){
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        vibrate = sharedPreferences.getBoolean("vibrate", true);
        fullscreen = sharedPreferences.getBoolean("fullscreen", true);
        keepScreen = sharedPreferences.getBoolean("keep_screen", false);
        enableFlashPosts = sharedPreferences.getBoolean("enable_flash_posts", true);
        useCSS = sharedPreferences.getBoolean("use_css", true);
        
        return sharedPreferences;
    }

}
