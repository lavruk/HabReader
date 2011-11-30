package net.meiolania.apps.habrahabr.activities;

import java.io.IOException;
import java.util.Formatter;

import net.meiolania.apps.habrahabr.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class PeopleShow extends ApplicationActivity{
    private String link;
    private String userName;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_show);
        
        Bundle extras = getIntent().getExtras();
        link = extras.getString("link");
        
        setActionBar();
        loadMan();
    }
    
    private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.people);
        actionBar.addAction(new ShareAction());
    }
    
    private class ShareAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_share;
        }

        public void performAction(View view){
            final Intent intent = new Intent(Intent.ACTION_SEND);
            final Formatter formatter = new Formatter();
            String shareText = formatter.format("%s - %s #HabraHabr #HabReader", userName, link).toString();

            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_link_qa));
            intent.putExtra(Intent.EXTRA_TEXT, shareText);

            startActivity(Intent.createChooser(intent, getString(R.string.share)));
        }

    }
    
    private void loadMan(){
        new LoadMan().execute();
    }
    
    private class LoadMan extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;
        
        @Override
        protected Void doInBackground(Void... params){
            try{
                Log.d("meiolania", link);
                
                Document document = Jsoup.connect(link).get();
                
                
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        
        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(PeopleShow.this);
            progressDialog.setMessage(getString(R.string.loading_man_show));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
        
        @Override
        protected void onPostExecute(Void result){
            progressDialog.dismiss();
        }
        
    }
    
}