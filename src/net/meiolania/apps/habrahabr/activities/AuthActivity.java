package net.meiolania.apps.habrahabr.activities;

import java.util.regex.Pattern;

import net.meiolania.apps.habrahabr.R;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthActivity extends AbstractionActivity {
    public static final String LOGIN_URL = "http://habrahabr.ru/login/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	setContentView(R.layout.auth_activity);

	showAuthPage();
    }

    private void showAuthPage() {
	WebView content = (WebView) findViewById(R.id.auth_content);
	
	content.setWebViewClient(new WebViewClient(){
	    @Override
	    public void onPageStarted(WebView view, String url, Bitmap favicon) {
	        super.onPageStarted(view, url, favicon);
	        
	        // We handle redirect to main page after auth
	        if(!url.contains("/login/")){
	            String cookies[] = Pattern.compile(";").split(CookieManager.getInstance().getCookie(LOGIN_URL));
	            
	            for(String cookie : cookies)
	        	Log.v("Test", cookie);
	        }
	    }
	});
	content.getSettings().setSupportZoom(true);
	content.getSettings().setBuiltInZoomControls(true);
	content.getSettings().setJavaScriptEnabled(true);

	content.loadUrl(LOGIN_URL);
    }

    @Override
    protected OnClickListener getConnectionDialogListener() {
	return new OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		finish();
	    }
	};
    }

}