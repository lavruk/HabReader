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

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.CommentsAdapter;
import net.meiolania.apps.habrahabr.data.CommentsData;
import net.meiolania.apps.habrahabr.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

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
        
        if(UIUtils.isHoneycombOrHigher())
            menu.removeItem(R.id.to_home);
        else
            menu.removeItem(R.id.update);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.update:
                loadComments();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setActionBar(){
        if(!UIUtils.isHoneycombOrHigher()){
            ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
            actionBar.setTitle(R.string.comments);
            actionBar.addAction(new UpdateAction());
        }else{
            ActionBar actionBarView = (ActionBar) findViewById(R.id.actionbar);
            actionBarView.setVisibility(View.GONE);
            
            android.app.ActionBar actionBar = getActionBar();
            actionBar.setTitle(R.string.comments);
            actionBar.setHomeButtonEnabled(true);
        }
    }
    
    private class UpdateAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_update;
        }

        public void performAction(View view){
            loadComments();
        }

    }

    private void loadComments(){
        new LoadComments().execute();
    }
    
    private int parseScore(String score){
        int commentRating = score.charAt(0) == '–' ? -1 : +1;
        try{
            commentRating *= Integer.valueOf(score.substring(1));
        }catch(NumberFormatException e){
            commentRating = 0;
        }
        return commentRating;
    }

    private class LoadComments extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect(link).get();
                Elements comments = document.select("div.comment_item");

                for(Element comment : comments){
                    CommentsData commentsData = new CommentsData();

                    Element userName = comment.select("a.username").first();
                    Element message = comment.select("div.message").first();
                    Element score = comment.select("span.score").first();

                    commentsData.setAuthor(userName.text());
                    commentsData.setAuthorLink(userName.attr("abs:href"));
                    commentsData.setText(message.text());
                    commentsData.setScore(parseScore(score.text()));

                    commentsDataList.add(commentsData);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            catch(NumberFormatException e){
                //ignore it
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
                ListView listView = (ListView) findViewById(R.id.comments_list);
                listView.setAdapter(new CommentsAdapter(PostsCommentsShow.this, commentsDataList));
            }
            progressDialog.dismiss();
        }

    }

}