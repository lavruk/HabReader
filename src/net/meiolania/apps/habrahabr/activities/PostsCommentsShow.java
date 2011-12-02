package net.meiolania.apps.habrahabr.activities;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.CommentsAdapter;
import net.meiolania.apps.habrahabr.data.CommentsData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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
                ListView listView = (ListView)PostsCommentsShow.this.findViewById(R.id.comments_list);
                listView.setAdapter(new CommentsAdapter(PostsCommentsShow.this, commentsDataList));
            }
            progressDialog.dismiss();
        }

    }

}