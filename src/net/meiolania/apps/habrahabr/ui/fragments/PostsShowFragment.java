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

package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;
import java.util.Formatter;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.ui.activities.PostsCommentsActivity;
import net.meiolania.apps.habrahabr.ui.activities.PostsShowActivity;
import net.meiolania.apps.habrahabr.utils.VibrateUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

public class PostsShowFragment extends ApplicationFragment{
    public final static String LOG_TAG = "PostsShowFragment.HabReader";
    private String link;
    private String title;
    private boolean isFullView = false;
    private LoadPost loadPost;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);

        if(link != null && link.length() > 0)
            loadPost();
        else{
            Log.e(LOG_TAG, "Can't display a post: " + ((link != null) ? link : "no link"));
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if(loadPost != null)
            loadPost.cancel(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(container == null)
            return null;

        View view = inflater.inflate(R.layout.posts_show_fragment, container, false);

        if(isFullView){
            Button more = (Button) view.findViewById(R.id.more);
            more.setVisibility(View.GONE);
        }else{
            Button more = (Button) view.findViewById(R.id.more);
            more.setOnClickListener(new OnClickListener(){
                public void onClick(View v){
                    startShowActivity();
                }
            });
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.posts_show_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(preferences.isVibrate())
            VibrateUtils.doVibrate(getActivity());
        switch(item.getItemId()){
            case R.id.show_comments:
                startCommentsActivity();
                break;
            case R.id.share:
                createShareIntent();
                break;
        }
        return true;
    }

    private void startCommentsActivity(){
        Intent intent = new Intent(getActivity(), PostsCommentsActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }

    private void createShareIntent(){
        final Intent intent = new Intent(Intent.ACTION_SEND);
        final Formatter formatter = new Formatter();
        String shareText = formatter.format("%s - %s #HabraHabr #HabReader", title, link).toString();

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_link_post));
        intent.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    private void startShowActivity(){
        Intent intent = new Intent(getActivity(), PostsShowActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }

    public void setIsFullView(boolean isFullView){
        this.isFullView = isFullView;
    }

    public String getTitle(){
        return title;
    }

    public void setLink(String link){
        this.link = link;
    }

    public String getLink(){
        return link;
    }

    private void loadPost(){
        if(ConnectionApi.isConnection(getActivity())){
            loadPost = new LoadPost();
            loadPost.execute();
        }
    }

    private class LoadPost extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;
        private String content;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect(link).get();

                Element contentElement = document.select("div.content").first();
                Element titleElement = document.select("span.post_title").first();

                title = titleElement.text();

                /*
                 * http://stackoverflow.com/questions/3961589/android-webview-and-loaddata If you now how solve this problem an another way
                 * please sumbit a patch.
                 */
                content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                if(preferences.isUseCSS())
                    content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";
                content += contentElement.outerHtml();
            }
            catch(IOException e){

            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            if(isFullView){
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.loading_post_show));
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled()){
                try{
                    WebView webView = (WebView) getActivity().findViewById(R.id.content);
                    webView.getSettings().setPluginsEnabled(preferences.isEnableFlashPosts());
                    webView.getSettings().setSupportZoom(true);
                    webView.getSettings().setBuiltInZoomControls(true);
                    // webView.loadData(content, "text/html", "UTF-8");
                    webView.loadDataWithBaseURL("", content, "text/html", "UTF-8", null);

                    // TODO: save post to read in the offline
                }
                catch(NullPointerException e){
                }
            }
            if(isFullView)
                progressDialog.dismiss();
        }

    }

}