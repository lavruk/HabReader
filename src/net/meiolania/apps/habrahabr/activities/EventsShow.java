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

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.utils.Vibrate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class EventsShow extends ApplicationActivity{
    private String link;
    private String title;
    private String description;
    private String placeInfo;
    private String priceInfo;
    private String linkInfo;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_show);

        Bundle extras = getIntent().getExtras();
        link = extras.getString("link");

        setActionBar();
        loadEvent();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.events_show, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(Preferences.vibrate)
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
        actionBar.setTitle(R.string.events);
        actionBar.addAction(new ShareAction());
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
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_link_event));
            intent.putExtra(Intent.EXTRA_TEXT, shareText);

            startActivity(Intent.createChooser(intent, getString(R.string.share)));
        }

    }

    private void loadEvent(){
        new LoadEvent().execute();
    }

    private class LoadEvent extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect(link).get();

                Element titleElement = document.select("h1.title").first();
                Element descriptionElement = document.select("div.description").first();
                Elements blockInfo = document.select("div.info_block > dl > dd");

                int i = 1;
                for(Element info : blockInfo){
                    if(i == 1){
                        placeInfo = info.text();
                    }else if(i == 2){
                        priceInfo = info.text();
                    }else{
                        linkInfo = info.outerHtml();
                    }
                    i++;
                }
                
                title = titleElement.text();
                
                /*
                 * http://stackoverflow.com/questions/3961589/android-webview-and-loaddata
                 * If you now how solve this problem an another way please sumbit a patch.
                 */
                description = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                description += descriptionElement.outerHtml();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(EventsShow.this);
            progressDialog.setMessage(getString(R.string.loading_events_show));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled()){
                TextView titleView = (TextView)findViewById(R.id.title);
                TextView placeInfoView = (TextView)findViewById(R.id.place_info);
                TextView priceInfoView = (TextView)findViewById(R.id.price_info);
                TextView linkInfoView = (TextView)findViewById(R.id.link_info);
                WebView descriptionView = (WebView)findViewById(R.id.description);
                
                titleView.setText(title);
                placeInfoView.setText(placeInfo);
                priceInfoView.setText(priceInfo);
                
                linkInfoView.setText(Html.fromHtml(linkInfo));
                linkInfoView.setMovementMethod(LinkMovementMethod.getInstance());
                
                descriptionView.getSettings().setPluginsEnabled(true);
                descriptionView.getSettings().setSupportZoom(true);
                descriptionView.getSettings().setBuiltInZoomControls(true);
                descriptionView.loadData(description, "text/html", "UTF-8");
            }
            progressDialog.dismiss();
        }

    }

}