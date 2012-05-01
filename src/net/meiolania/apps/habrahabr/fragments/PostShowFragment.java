/*
Copyright 2012 Andrey Zaytsev

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

package net.meiolania.apps.habrahabr.fragments;

import java.io.IOException;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.PostsFullData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PostShowFragment extends SherlockFragment{
    protected String url;

    public PostShowFragment(){
    }

    public PostShowFragment(String url){
        this.url = url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        loadPost();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.posts_show_activity, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.posts_show_activity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    protected void loadPost(){
        new LoadPost().execute();
    }

    protected final class LoadPost extends AsyncTask<Void, Void, PostsFullData>{
        private ProgressDialog progressDialog;

        @Override
        protected PostsFullData doInBackground(Void... params){
            PostsFullData postsFullData = new PostsFullData();
            try{
                Document document = Jsoup.connect(url).get();
                Element title = document.select("span.post_title").first();
                Element hubs = document.select("div.hubs").first();
                Element content = document.select("div.content").first();
                Element date = document.select("div.published").first();
                Element author = document.select("div.author > a").first();

                postsFullData.setUrl(url);
                postsFullData.setTitle(title.text());
                postsFullData.setHubs(hubs.text());
                postsFullData.setContent(content.html());
                postsFullData.setDate(date.text());
                postsFullData.setAuthor(author.text());
            }
            catch(IOException e){
            }
            return postsFullData;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(getSherlockActivity());
            progressDialog.setTitle(R.string.loading);
            progressDialog.setMessage(getString(R.string.loading_post));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(final PostsFullData result){
            getSherlockActivity().runOnUiThread(new Runnable(){
                public void run(){
                    if(!isCancelled()){
                        WebView content = (WebView) getSherlockActivity().findViewById(R.id.post_content);
                        content.getSettings().setPluginsEnabled(true);
                        content.getSettings().setSupportZoom(true);
                        content.getSettings().setBuiltInZoomControls(true);
                        content.loadDataWithBaseURL("", result.getContent(), "text/html", "UTF-8", null);
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

}