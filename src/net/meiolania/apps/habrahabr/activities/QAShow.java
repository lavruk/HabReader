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

package net.meiolania.apps.habrahabr.activities;

import java.io.IOException;
import java.util.Formatter;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.utils.Vibrate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class QAShow extends ApplicationActivity{
    private String link;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qa_show);

        Bundle extras = getIntent().getExtras();
        link = extras.getString("link");

        setActionBar();
        loadQuestion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.qa_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(preferences.isVibrate())
            Vibrate.doVibrate(this);
        switch(item.getItemId()){
            case R.id.show_in_browser:
                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.to_home:
                startActivity(new Intent(this, Dashboard.class));
                break;    
        }
        return true;
    }

    private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.qa);
        actionBar.addAction(new ShowCommentsAction());
        actionBar.addAction(new ShareAction());
    }
    
    private class ShowCommentsAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_comments;
        }

        public void performAction(View view){
            Intent intent = new Intent(QAShow.this, QACommentsShow.class);
            intent.putExtra("link", link);
            startActivity(intent);
        }
        
    }

    private class ShareAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_share;
        }

        public void performAction(View view){
            final Intent intent = new Intent(Intent.ACTION_SEND);
            final Formatter formatter = new Formatter();
            String shareText = formatter.format("%s - %s #HabraHabr #HabReader", title, link).toString();

            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_link_qa));
            intent.putExtra(Intent.EXTRA_TEXT, shareText);

            startActivity(Intent.createChooser(intent, getString(R.string.share)));
        }

    }

    private void loadQuestion(){
        new LoadQuestion().execute();
    }

    private class LoadQuestion extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;
        private String content;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect(link).get();

                Element contentElement = document.select("div.content").first();
                Element titleElement = document.select("span.topic").first();

                title = titleElement.text();
                
                /*
                 * http://stackoverflow.com/questions/3961589/android-webview-and-loaddata
                 * If you now how solve this problem an another way please sumbit a patch.
                 */
                content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                if(preferences.isUseCSS())
                    content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";
                content += contentElement.outerHtml();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(QAShow.this);
            progressDialog.setMessage(getString(R.string.loading_qa_show));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled()){
                WebView webView = (WebView)QAShow.this.findViewById(R.id.question_text);
                webView.getSettings().setPluginsEnabled(true);
                webView.getSettings().setSupportZoom(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.loadData(content, "text/html", "UTF-8");
            }
            progressDialog.dismiss();
        }

    }

}