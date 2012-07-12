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

package net.meiolania.apps.habrahabr.fragments.posts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.CommentsAdapter;
import net.meiolania.apps.habrahabr.data.CommentsData;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import net.meiolania.apps.habrahabr.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;

public class PostsCommentsFragment extends SherlockListFragment{
    public final static String LOG_TAG = "PostsCommentsFragment";
    public final static int MENU_OPEN_COMMENT_IN_BROWSER = 0;
    public final static int MENU_OPEN_AUTHOR_PROFILE = 1;
    protected final ArrayList<CommentsData> commentsDatas = new ArrayList<CommentsData>();
    protected CommentsAdapter commentsAdapter;
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
        
        commentsAdapter = new CommentsAdapter(getSherlockActivity(), commentsDatas);
        setListAdapter(commentsAdapter);
        
        registerForContextMenu(getListView());
        
        loadList();
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.add(0, MENU_OPEN_COMMENT_IN_BROWSER, 0, R.string.open_comment_in_browser);
        menu.add(0, MENU_OPEN_AUTHOR_PROFILE, 0, R.string.open_author_profile);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo)item.getMenuInfo();
        CommentsData commentsData = (CommentsData)getListAdapter().getItem(adapterContextMenuInfo.position);
        
        switch(item.getItemId()){
            case MENU_OPEN_COMMENT_IN_BROWSER:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(commentsData.getUrl())));
                break;
            case MENU_OPEN_AUTHOR_PROFILE:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(commentsData.getAuthorUrl())));
                break;
        }
        return super.onContextItemSelected(item);
    }

    protected void loadList(){
    	if(ConnectionUtils.isConnected(getSherlockActivity())){
    		new LoadComments().execute();
    		if(!UIUtils.isHoneycombOrHigher())
    			Toast.makeText(getSherlockActivity(), R.string.loading_comments, Toast.LENGTH_SHORT).show();
    	}
    }
    
    //TODO: need to think more about the algorithm
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
                Element linkToComment = comment.select("a.link_to_comment").first();
                //Element score = comment.select("span.score").first();
                
                //commentsData.setScore(score);
                commentsData.setUrl(linkToComment.attr("abs:href"));
                commentsData.setAuthorUrl(name.attr("abs:href"));
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
                        commentsAdapter.notifyDataSetChanged();
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                }
            });
        }

    }

}