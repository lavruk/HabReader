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
import net.meiolania.apps.habrahabr.adapters.PostsAdapter;
import net.meiolania.apps.habrahabr.data.PostsData;
import net.meiolania.apps.habrahabr.utils.UIUtils;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class Posts extends ApplicationActivity{
    private final ArrayList<PostsData> postsDataList = new ArrayList<PostsData>();
    private PostsAdapter postsAdapter;
    private int page;
    private String link;
    private Bundle extras;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts);
        
        extras = getIntent().getExtras();
        if(extras != null)
            link = extras.getString("link");
        
        setActionBar();
        loadList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.posts, menu);
        
        if(UIUtils.isHoneycombOrHigher())
            menu.removeItem(R.id.to_home);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.to_home:
                Intent intent = new Intent(this, Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void setActionBar(){
        if(!UIUtils.isHoneycombOrHigher()){
            ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
            actionBar.setTitle(R.string.posts);
            actionBar.addAction(new LoadNextPageAction());
            actionBar.addAction(new UpdateAction());
        }else{
            ActionBar actionBarView = (ActionBar) findViewById(R.id.actionbar);
            actionBarView.setVisibility(View.GONE);
            
            android.app.ActionBar actionBar = getActionBar();
            
            /*
            Tab tab = actionBar.newTab().setText(R.string.all_posts);
            actionBar.addTab(tab);
            
            tab = actionBar.newTab().setText(R.string.new_posts);
            actionBar.addTab(tab);
            */
            
            actionBar.setTitle(R.string.posts);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private class LoadNextPageAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_forward;
        }

        public void performAction(View view){
            loadList();
        }

    }

    private class UpdateAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_update;
        }

        public void performAction(View view){
            loadList();
        }

    }

    private void loadList(){
        ++page;
        new LoadPostsList().execute();
    }

    private class LoadPostsList extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params){
            Document document = null;
            Element blogTitle = null;
            try{
                if(extras != null)
                    document = Jsoup.connect(link + "page" + page + "/").get();
                else
                    document = Jsoup.connect("http://habrahabr.ru/blogs/page" + page + "/").get();

                Elements postsList = document.select("div.post");
                if(extras != null)
                    blogTitle = document.select("a.blog_title").first();

                for(Element post : postsList){
                    PostsData postsData = new PostsData();

                    Element title = post.select("a.post_title").first();
                    Element blog = post.select("a.blog_title").first();

                    postsData.setTitle(title.text());
                    
                    if(extras != null)
                        postsData.setBlog(blogTitle.text());
                    else
                        postsData.setBlog(blog.text());
                    
                    postsData.setLink(title.attr("abs:href"));

                    postsDataList.add(postsData);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(Posts.this);
            progressDialog.setMessage(getString(R.string.loading_posts_list));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                postsAdapter = new PostsAdapter(Posts.this, postsDataList);

                ListView listView = (ListView) Posts.this.findViewById(R.id.posts_list);
                listView.setAdapter(postsAdapter);
                listView.setOnItemClickListener(new OnItemClickListener(){
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                        PostsData postsData = postsDataList.get(position);

                        Intent intent = new Intent(Posts.this, PostsShow.class);
                        intent.putExtra("link", postsData.getLink());

                        Posts.this.startActivity(intent);
                    }
                });
            }else
                postsAdapter.notifyDataSetChanged();

            progressDialog.dismiss();
        }

    }

}