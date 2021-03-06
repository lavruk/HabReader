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

package net.meiolania.apps.habrahabr;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class Preferences {
    // Default tab for posts
    public final static String POSTS_DEFAULT_TAB_KEY = "posts_default_tab";
    public final static String POSTS_DEFAULT_TAB_DEFAULT = "1";
    // Default tab for Q&A
    public final static String QA_DEFAULT_TAB_KEY = "qa_default_tab";
    public final static String QA_DEFAULT_TAB_DEFAULT = "0";
    // Default tab for events
    public final static String EVENTS_DEFAULT_TAB_KEY = "events_default_tab";
    public final static String EVENTS_DEFAULT_TAB_DEFAULT = "0";
    // Additional layout for posts
    public final static String ADDITIONAL_LAYOUT_POSTS_KEY = "posts_additional_layout";
    public final static boolean ADDITIONAL_LAYOUT_POSTS_DEFAULT = true;
    // Additional layout for hubs
    public final static String ADDITIONAL_LAYOUT_HUBS_KEY = "hubs_additional_layout";
    public final static boolean ADDITIONAL_LAYOUT_HUBS_DEFAULT = true;
    // Additional layout for Q&A
    public final static String ADDITIONAL_LAYOUT_QA_KEY = "qa_additional_layout";
    public final static boolean ADDITIONAL_LAYOUT_QA_DEFAULT = true;
    // Additional layout for events
    public final static String ADDITIONAL_LAYOUT_EVENTS_KEY = "events_additional_layout";
    public final static boolean ADDITIONAL_LAYOUT_EVENTS_DEFAULT = true;
    // Fullscreen
    public final static String FULLSCREEN_KEY = "fullscreen";
    public final static boolean FULLSCREEN_DEFAULT = false;
    // Keepscreen
    public final static String KEEPSCREEN_KEY = "keepscreen";
    public final static boolean KEEPSCREEN_DEFAULT = false;
    // Share text
    public final static String SHARE_TEXT_KEY = "share_text";
    public final static String SHARE_TEXT_DEFAULT = "$link$ - $title$ #HabReader";

    private static Preferences preferences = null;
    private static SharedPreferences sharedPreferences;

    private Preferences(Context context) {
	sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Preferences getInstance(Context context) {
	return preferences != null ? preferences : (preferences = new Preferences(context));
    }

    public SharedPreferences getSharedPreferences() {
	return sharedPreferences;
    }

    public boolean getFullScreen() {
	return sharedPreferences.getBoolean(FULLSCREEN_KEY, FULLSCREEN_DEFAULT);
    }

    public boolean getKeepScreen() {
	return sharedPreferences.getBoolean(KEEPSCREEN_KEY, KEEPSCREEN_DEFAULT);
    }

    public int getPostsDefaultTab() {
	return Integer.parseInt(sharedPreferences.getString(POSTS_DEFAULT_TAB_KEY, POSTS_DEFAULT_TAB_DEFAULT));
    }

    public int getEventsDefaultTab() {
	return Integer.parseInt(sharedPreferences.getString(EVENTS_DEFAULT_TAB_KEY, EVENTS_DEFAULT_TAB_DEFAULT));
    }

    public int getQaDefaultTab() {
	return Integer.parseInt(sharedPreferences.getString(QA_DEFAULT_TAB_KEY, QA_DEFAULT_TAB_DEFAULT));
    }

    public boolean getAdditionalEvents() {
	return sharedPreferences.getBoolean(ADDITIONAL_LAYOUT_EVENTS_KEY, ADDITIONAL_LAYOUT_EVENTS_DEFAULT);
    }

    public boolean getAdditionalPosts() {
	return sharedPreferences.getBoolean(ADDITIONAL_LAYOUT_POSTS_KEY, ADDITIONAL_LAYOUT_POSTS_DEFAULT);
    }

    public boolean getAdditionalHubs() {
	return sharedPreferences.getBoolean(ADDITIONAL_LAYOUT_HUBS_KEY, ADDITIONAL_LAYOUT_HUBS_DEFAULT);
    }

    public boolean getAdditionalQa() {
	return sharedPreferences.getBoolean(ADDITIONAL_LAYOUT_QA_KEY, ADDITIONAL_LAYOUT_QA_DEFAULT);
    }
    
    public String getShareText() {
	return sharedPreferences.getString(SHARE_TEXT_KEY, SHARE_TEXT_DEFAULT);
    }

    public static void saveViewScale(Context context, float scale) {
	SharedPreferences.Editor editor = sharedPreferences.edit();
	editor.putFloat(context.getString(R.string.saved_user_scale), scale);
	editor.commit();
    }

    public static int getViewScale(Context context) {
	float userScale = sharedPreferences.getFloat(context.getString(R.string.saved_user_scale), 0.0f);
	return (int) (100 * userScale);
    }

}