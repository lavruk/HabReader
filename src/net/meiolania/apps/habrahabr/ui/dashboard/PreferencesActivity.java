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

package net.meiolania.apps.habrahabr.ui.dashboard;

import net.meiolania.apps.habrahabr.R;
import net.robotmedia.billing.BillingController;
import net.robotmedia.billing.BillingController.IConfiguration;
import net.robotmedia.billing.BillingRequest.ResponseCode;
import net.robotmedia.billing.helper.AbstractBillingObserver;
import net.robotmedia.billing.model.Transaction.PurchaseState;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class PreferencesActivity extends PreferenceActivity{
    public final static String ANDROID_DONATE_ITEM = "donate.1dollar";
    private AbstractBillingObserver billingObserver;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        
        signIn();
        showAttentionForFullscreen();
        rateApplication();
        sendReview();
        share();
        donate();
    }

    private void donate(){
        final Preference donate = (Preference) findPreference("donate");

        billingObserver = new AbstractBillingObserver(this){

            public void onRequestPurchaseResponse(String itemId, ResponseCode response){
            }

            public void onPurchaseStateChanged(String itemId, PurchaseState state){
            }

            public void onBillingChecked(boolean supported){
                if(!supported)
                    donate.setEnabled(false);
                else if(!billingObserver.isTransactionsRestored())
                    BillingController.restoreTransactions(PreferencesActivity.this);
            }
        };

        // BillingController.setDebug(true);
        BillingController.setConfiguration(new IConfiguration(){

            public String getPublicKey(){
                return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj+ZiahGZpgCRnvogALUM15bEIwTFjcXCIrsbwE8DdEocJGH5FHkwIdceXTSowUhIOaukOLmG1+R+2PqEnRnYsEdqBcCmSnYxT/LRGhoKWU2BDlOUCtm0KRGBFywV4BavgN21GLRNgm5jl67LjEy6jwKfBFddLi0T/jzySoHtJkLhVwvuPNpmv99il1sQJArgDWIa/grS7rOH69I7BJRYugNk9smONONamsgtN3JUvH0qqIF371En9nMJlFdLLIq4UwSZ7IhqVvwexFpvx6J5k63YXfB5uanrfGXJ6sv9GOCFoqfHkmS1016I60hbIH16ye0b65gfxFd1XVRQrIhW3QIDAQAB";
            }

            public byte[] getObfuscationSalt(){
                return new byte[]{ 41, -90, -116, -41, 66, -53, 122, -110, -127, -96, -88, 77, 127, 115, 1, 73, 57, 110, 48, -116 };
            }

        });
        BillingController.registerObserver(billingObserver);
        BillingController.checkBillingSupported(this);

        donate.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference){
                BillingController.requestPurchase(PreferencesActivity.this, ANDROID_DONATE_ITEM, true);
                return false;
            }
        });

        boolean purchased = BillingController.isPurchased(this, ANDROID_DONATE_ITEM);
        if(purchased){
            donate.setTitle(R.string.preferences_donate_thanks);
            donate.setSummary(R.string.preferences_donate_thanks_summary);
            donate.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy(){
        BillingController.unregisterObserver(billingObserver);
        super.onDestroy();
    }

    private void sendReview(){
        final Preference review = (Preference) findPreference("send_review");
        review.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference){
                // Some problems here. Without this a field "to" is empty
                String[] emails = { "support+habreader@meiolania.net", "" };

                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, emails);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                intent.setType("text/html");
                startActivity(Intent.createChooser(intent, getString(R.string.preferences_send_review)));
                return false;
            }
        });
    }

    private void share(){
        final Preference share = (Preference) findPreference("share");
        share.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference){
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.preferences_share_text, "https://market.android.com/details?id=net.meiolania.apps.habrahabr"));
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getString(R.string.preferences_share)));
                return false;
            }
        });
    }

    private void signIn(){
        //TODO: show later, when sign in will be completed
        /*
        final Preference signIn = (Preference) findPreference("sign_in");
        signIn.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference){
                startActivity(new Intent(PreferencesActivity.this, SignInActivity.class));
                return false;
            }
        });
        */
    }

    private void showAttentionForFullscreen(){
        final Preference fullScreen = (Preference) findPreference("fullscreen");
        fullScreen.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference){
                Toast.makeText(PreferencesActivity.this, R.string.preferences_fullscreen_summary_1, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void rateApplication(){
        final Preference rateApplication = (Preference) findPreference("rate_application");
        rateApplication.setOnPreferenceClickListener(new OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference){
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.meiolania.apps.habrahabr"));
                    startActivity(intent);
                }
                catch(ActivityNotFoundException e){
                }
                return false;
            }
        });
    }
}