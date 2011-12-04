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
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.CommentsAdapter;
import net.meiolania.apps.habrahabr.data.CommentsData;
import net.meiolania.apps.habrahabr.utils.Vibrate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;

public class PostsCommentsShow extends ApplicationActivity{
    private final ArrayList<CommentsData> commentsDataList = new ArrayList<CommentsData>();
    private String link;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_show);

        Bundle extras = getIntent().getExtras();
        link = extras.getString("link");

        setActionBar();
        loadComments();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.posts_comments_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(Preferences.vibrate)
            Vibrate.doVibrate(this);
        switch(item.getItemId()){
            case R.id.to_home:
                startActivity(new Intent(this, Dashboard.class));
                break;
        }
        return true;
    }

    private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.comments);
    }

    private void loadComments(){
        new LoadComments().execute();
    }

    private class LoadComments extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect(link).get();
                Elements comments = document.select("div.comment");

                for(Element comment : comments){
                    CommentsData commentsData = new CommentsData();

                    Element userName = comment.select("a.username").first();
                    Element message = comment.select("div.message").first();

                    commentsData.setAuthor(userName.text());
                    commentsData.setAuthorLink(userName.attr("abs:href"));
                    commentsData.setText(message.text());

                    commentsDataList.add(commentsData);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(PostsCommentsShow.this);
            progressDialog.setMessage(getString(R.string.loading_comments));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled()){
                ListView listView = (ListView) PostsCommentsShow.this.findViewById(R.id.comments_list);
                listView.setAdapter(new CommentsAdapter(PostsCommentsShow.this, commentsDataList));
            }
            progressDialog.dismiss();
        }

    }

}