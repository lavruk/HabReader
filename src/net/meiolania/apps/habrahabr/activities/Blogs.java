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
import net.meiolania.apps.habrahabr.adapters.BlogsAdapter;
import net.meiolania.apps.habrahabr.data.BlogsData;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class Blogs extends ApplicationActivity{
    private final ArrayList<BlogsData> blogsDataList = new ArrayList<BlogsData>();
    private BlogsAdapter blogsAdapter;
    private int page;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blogs);

        setActionBar();
        loadList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.blogs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(preferences.isVibrate())
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
        actionBar.setTitle(R.string.blogs);
        actionBar.addAction(new LoadNextPageAction());
    }

    private class LoadNextPageAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_forward;
        }

        public void performAction(View view){
            loadList();
        }

    }

    private void loadList(){
        ++page;
        new LoadBlogsList().execute();
    }

    private class LoadBlogsList extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect("http://habrahabr.ru/bloglist/page" + page + "/").get();
                Elements blogRows = document.select("li.blog-row");

                for(Element blog : blogRows){
                    BlogsData blogsData = new BlogsData();

                    Element link = blog.select("a.title").first();
                    Element statistics = blog.select("div.stat").first();

                    blogsData.setTitle(link.text());
                    blogsData.setStatistics(statistics.text());
                    blogsData.setLink(link.attr("abs:href"));

                    blogsDataList.add(blogsData);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(Blogs.this);
            progressDialog.setMessage(getString(R.string.loading_blogs_list));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                blogsAdapter = new BlogsAdapter(Blogs.this, blogsDataList);

                ListView listView = (ListView) Blogs.this.findViewById(R.id.blogs_list);
                listView.setAdapter(blogsAdapter);
                listView.setOnItemClickListener(new OnItemClickListener(){
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                        BlogsData blogsData = blogsDataList.get(position);

                        Intent intent = new Intent(Blogs.this, BlogsPosts.class);
                        intent.putExtra("link", blogsData.getLink());

                        Blogs.this.startActivity(intent);
                    }
                });
            }else
                blogsAdapter.notifyDataSetChanged();

            progressDialog.dismiss();
        }

    }

}