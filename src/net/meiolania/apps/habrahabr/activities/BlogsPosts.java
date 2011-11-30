package net.meiolania.apps.habrahabr.activities;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.PostsAdapter;
import net.meiolania.apps.habrahabr.data.PostsData;
import net.meiolania.apps.habrahabr.utils.Vibrate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class BlogsPosts extends ApplicationActivity{
    private final ArrayList<PostsData> postsDataList = new ArrayList<PostsData>();
    private PostsAdapter postsAdapter;
    private int page;
    private String link;
    private Document document;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts);

        Bundle extras = getIntent().getExtras();
        link = extras.getString("link");

        setActionBar();
        loadList();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_SEARCH:
                startActivity(new Intent(this, PostsSearch.class));
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.posts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(Preferences.vibrate)
            Vibrate.doVibrate(this);
        switch(item.getItemId()){
            case R.id.favorites:
                startActivity(new Intent(this, FavoritesPosts.class));
                break;
        }
        return true;
    }

    private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.posts);
        actionBar.addAction(new LoadNextPageAction());
        actionBar.addAction(new UpdateAction());
        actionBar.addAction(new IntentAction(this, new Intent(this, PostsSearch.class), R.drawable.actionbar_ic_search));
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
            Element blogTitle;
            try{
                document = Jsoup.connect(link + "page" + page + "/").get();

                Elements postsList = document.select("div.post");
                blogTitle = document.select("a.blog_title").first();

                for(Element post : postsList){
                    PostsData postsData = new PostsData();

                    Element title = post.select("a.post_title").first();

                    postsData.setTitle(title.text());
                    postsData.setBlog(blogTitle.text());
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
            progressDialog = new ProgressDialog(BlogsPosts.this);
            progressDialog.setMessage(getString(R.string.loading_posts_list));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                postsAdapter = new PostsAdapter(BlogsPosts.this, postsDataList);

                ListView listView = (ListView) BlogsPosts.this.findViewById(R.id.posts_list);
                listView.setAdapter(postsAdapter);
                listView.setOnItemClickListener(new OnItemClickListener(){
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                        PostsData postsData = postsDataList.get(position);

                        Intent intent = new Intent(BlogsPosts.this, PostsShow.class);
                        intent.putExtra("link", postsData.getLink());

                        BlogsPosts.this.startActivity(intent);
                    }
                });
            }else
                postsAdapter.notifyDataSetChanged();

            progressDialog.dismiss();
        }

    }

}