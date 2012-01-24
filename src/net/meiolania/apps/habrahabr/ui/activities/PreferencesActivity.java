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

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.WindowManager;
import android.widget.Toast;

public class PreferencesActivity extends PreferenceActivity{
    protected net.meiolania.apps.habrahabr.Preferences preferences;
    protected SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        preferences = new net.meiolania.apps.habrahabr.Preferences(this);
        sharedPreferences = preferences.getSharedPreferences();

        if(preferences.isFullscreen())
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(UIUtils.isHoneycombOrHigher())
            setTheme(android.R.style.Theme_Holo_Light);

        addPreferencesFromResource(R.xml.preferences);

        //signIn();
        showAttentionForFullscreen();
        rateApplication();
        sendReview();
        share();
    }

    private void sendReview(){
        Preference review = (Preference) findPreference("send_review");
        review.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference){
                // Some problems here. Without this a field "to" is empty
                String[] emails = { "support+habreader@meiolania.net", "" };

                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, emails);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getString(R.string.preferences_send_review)));
                return false;
            }
        });
    }

    private void share(){
        Preference share = (Preference) findPreference("share");
        share.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference){
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                intent.putExtra(android.content.Intent.EXTRA_TEXT,
                        getString(R.string.preferences_share_text, "https://market.android.com/details?id=net.meiolania.apps.habrahabr"));
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getString(R.string.preferences_share)));
                return false;
            }
        });
    }

    private void signIn(){
        Preference signIn = (Preference) findPreference("sign_in");
        signIn.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference){
                startActivity(new Intent(PreferencesActivity.this, SignInActivity.class));
                return false;
            }
        });
    }

    private void showAttentionForFullscreen(){
        Preference fullScreen = (Preference) findPreference("fullscreen");
        fullScreen.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference){
                Toast.makeText(PreferencesActivity.this, R.string.preferences_fullscreen_summary_1, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void rateApplication(){
        Preference rateApplication = (Preference) findPreference("rate_application");
        rateApplication.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.meiolania.apps.habrahabr"));
                startActivity(intent);
                return false;
            }
        });
    }
}