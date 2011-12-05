package net.meiolania.apps.habrahabr.activities;

import java.io.IOException;
import java.util.Formatter;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

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
        private String ratingPlace;
        private String birthday;
        private String place;
        private String summary;
        private String tags;
        
        @Override
        protected Void doInBackground(Void... params){
            try{
                Log.d("meiolania", link);
                
                Document document = Jsoup.connect(link).get();
                
                Element userNameElement = document.select("dl.user-name > dt.fn").first();
                Element ratingPlaceElement = document.select("dl.user-name > dd.rating-place").first();
                Element birthdayElement = document.select("dd.bday").first();
                //Place
                Element countryElement = document.select("a.country-name").first();
                Element regionElement = document.select("a.region").first();
                Element cityElement = document.select("city").first();
                //Others
                Element summaryElement = document.select("dd.summary").first();
                Element tagsElement = document.select("ul#people-tags").first();
                
                userName = userNameElement.text();
                ratingPlace = ratingPlaceElement.text();
                birthday = birthdayElement.text();
                
                StringBuilder placeBuilder = new StringBuilder();
                
                if(countryElement.hasText()){
                    placeBuilder.append(countryElement.text());
                    if(regionElement != null && regionElement.hasText())
                        placeBuilder.append(", " + regionElement.text());
                    if(cityElement != null && cityElement.hasText())
                        placeBuilder.append(", " + cityElement.text());
                }else
                    placeBuilder.append(getString(R.string.place_undefined));
                
                place = placeBuilder.toString();
                
                summary = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                if(Preferences.useCSS)
                    summary += "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";
                summary += summaryElement.outerHtml();
                
                if(Preferences.useCSS)
                    tags = "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";
                tags += tagsElement.outerHtml();
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
            if(!isCancelled()){
                TextView userNameView = (TextView)findViewById(R.id.name);
                TextView ratingPlaceView = (TextView)findViewById(R.id.rating_place);
                TextView birthdayView = (TextView)findViewById(R.id.birthday);
                TextView placeView = (TextView)findViewById(R.id.place);
                TextView tagsView = (TextView)findViewById(R.id.tags);
                WebView summaryView = (WebView)findViewById(R.id.summary);
                
                userNameView.setText(userName);
                ratingPlaceView.setText(ratingPlace);
                birthdayView.setText(birthday);
                placeView.setText(place);
                
                tagsView.setText(Html.fromHtml(tags));
                tagsView.setMovementMethod(LinkMovementMethod.getInstance());
                
                summaryView.getSettings().setPluginsEnabled(true);
                summaryView.getSettings().setSupportZoom(true);
                summaryView.getSettings().setBuiltInZoomControls(true);
                summaryView.loadData(summary, "text/html", "UTF-8");
            }
            progressDialog.dismiss();
        }
        
    }
    
}