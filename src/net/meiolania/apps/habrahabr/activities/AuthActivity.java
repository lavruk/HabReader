package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.Auth;
import net.meiolania.apps.habrahabr.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class AuthActivity extends AbstractionActivity{
    public final static String CAPTCHA_URL = "http://habrahabr.ru/core/captcha/";
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);
        showActionBar();
        showCaptcha();
        doAuth();
    }
    
    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.auth_preferences);
    }
    
    private void showCaptcha(){
        final ImageView captcha = (ImageView)findViewById(R.id.captcha_image);
        
        DisplayImageOptions options = new DisplayImageOptions.Builder().build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options).build();
        
        final ImageLoader loader = ImageLoader.getInstance();
        loader.init(configuration);
        loader.displayImage(CAPTCHA_URL, captcha);
        
        captcha.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                loader.displayImage(CAPTCHA_URL, captcha);
            }
        });
    }
    
    private void doAuth(){
        Button auth = (Button)findViewById(R.id.auth);
        auth.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                EditText login = (EditText)findViewById(R.id.login);
                EditText password = (EditText)findViewById(R.id.password);
                EditText captcha = (EditText)findViewById(R.id.captcha);
                
                new AsyncAuth().execute(login.getText().toString(), password.getText().toString(), captcha.getText().toString());
            }
        });
    }
    
    private class AsyncAuth extends AsyncTask<String, Void, Void>{
        private ProgressDialog progressDialog;
        
        @Override
        protected Void doInBackground(String... params){
            String login = params[0];
            String password = params[1];
            String captcha = params[2];
            Auth.doAuth(AuthActivity.this, login, password, captcha);
            return null;
        }
        
        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(AuthActivity.this);
            progressDialog.setMessage(getString(R.string.auth_preferences));
            progressDialog.show();
        }
        
        @Override
        protected void onPostExecute(Void result){
            progressDialog.dismiss();
        }
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, PreferencesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
}