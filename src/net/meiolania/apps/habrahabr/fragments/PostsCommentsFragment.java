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
import java.util.ArrayList;
import java.util.Hashtable;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.PostsCommentsAdapter;
import net.meiolania.apps.habrahabr.data.CommentsData;
import net.meiolania.apps.habrahabr.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;

public class PostsCommentsFragment extends SherlockListFragment{
    public final static String LOG_TAG = "PostsCommentsFragment";
    protected final ArrayList<CommentsData> commentsDatas = new ArrayList<CommentsData>();
    protected PostsCommentsAdapter postsCommentsAdapter;
    protected String url;

    public PostsCommentsFragment(){
    }

    public PostsCommentsFragment(String url){
        this.url = url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        postsCommentsAdapter = new PostsCommentsAdapter(getSherlockActivity(), commentsDatas);
        setListAdapter(postsCommentsAdapter);
        loadList();
    }

    protected void loadList(){
        new LoadComments().execute();
        if(!UIUtils.isHoneycombOrHigher())
            Toast.makeText(getSherlockActivity(), R.string.loading_comments, Toast.LENGTH_SHORT).show();
    }
    
    //TODO: okay. I need to think more about the algorithm
    protected final class LoadComments extends AsyncTask<Void, Void, Void>{
        private Hashtable<String, Boolean> containedComments = new Hashtable<String, Boolean>();
        
        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect(url).get();
                Elements comments = document.select("div.comment_item");
                parseComments(comments, 0);
            }
            catch(IOException e){
            }
            return null;
        }
        
        private void parseComments(Elements comments, int level){
            for(Element comment : comments){
                CommentsData commentsData = new CommentsData();
                
                String commentId = comment.attr("id");
                if(containedComments.containsKey(commentId))
                    continue;
                containedComments.put(commentId, true);
                
                Element name = comment.select("a.username").first();
                Element message = comment.select("div.message").first();
                
                commentsData.setAuthor(name.text());
                commentsData.setComment(message.text());
                commentsData.setLevel(level);
                
                commentsDatas.add(commentsData);
                
                Elements replyComments = comment.select("div.reply_comments > div.comment_item");
                parseComments(replyComments, level+1);
            }
        }

        @Override
        protected void onPostExecute(Void result){
            getSherlockActivity().runOnUiThread(new Runnable(){
                public void run(){
                    if(!isCancelled())
                        postsCommentsAdapter.notifyDataSetChanged();
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                }
            });
        }

    }

}