package net.meiolania.apps.habrahabr.fragments;

import java.io.IOException;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.QaFullData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class QaShowFragment extends SherlockFragment{
    public final static String LOG_TAG = "QaShowFragment";
    protected String url;

    public QaShowFragment(){
    }

    public QaShowFragment(String url){
        this.url = url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        loadInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.qa_show_activity, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.qa_show_activity, menu);
    }

    protected void loadInfo(){
        new LoadQuestion().execute();
    }

    protected final class LoadQuestion extends AsyncTask<Void, Void, QaFullData>{
        private ProgressDialog progressDialog;

        @Override
        protected QaFullData doInBackground(Void... params){
            QaFullData qaFullData = new QaFullData();
            try{
                Log.i(LOG_TAG, "Loading " + url);

                Document document = Jsoup.connect(url).get();
                Element title = document.select("span.post_title").first();
                Element hubs = document.select("div.hubs").first();
                Element content = document.select("div.content").first();
                Element tags = document.select("ul.tags").first();
                Element date = document.select("div.published").first();
                Element author = document.select("div.author > a").first();
                Element answers = document.select("span#comments_count").first();
                
                qaFullData.setTitle(title.text());
                qaFullData.setHubs(hubs.text());
                qaFullData.setContent(content.html());
                qaFullData.setTags(tags.text());
                qaFullData.setDate(date.text());
                qaFullData.setAuthor(author.text());
                qaFullData.setAnswers(answers.text());
            }
            catch(IOException e){
            }
            return qaFullData;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(getSherlockActivity());
            progressDialog.setTitle(R.string.loading);
            progressDialog.setMessage(getString(R.string.loading_question));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(final QaFullData result){
            getSherlockActivity().runOnUiThread(new Runnable(){
                public void run(){
                    if(!isCancelled()){
                        WebView content = (WebView)getSherlockActivity().findViewById(R.id.qa_content);
                        content.getSettings().setPluginsEnabled(true);
                        content.getSettings().setBuiltInZoomControls(true);
                        content.getSettings().setSupportZoom(true);
                        content.loadDataWithBaseURL("", result.getContent(), "text/html", "UTF-8", null);
                    }
                }
            });
            progressDialog.dismiss();
        }

    }

}