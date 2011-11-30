package net.meiolania.apps.habrahabr.activities;

import java.io.IOException;

import net.meiolania.apps.habrahabr.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.markupartist.android.widget.ActionBar;

public class PostsCommentsShow extends ApplicationActivity{
    private String link;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_comments_show);

        Bundle extras = getIntent().getExtras();
        link = extras.getString("link");

        setActionBar();
        loadComments();
    }

    private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.posts);
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
                    loadComment(comment);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        
        private void loadComment(Element comment){
            if(comment.parent().hasClass("reply_comments"))
                loadComment(comment.parent());
            else{
                
            }
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
            progressDialog.dismiss();
        }

    }

}