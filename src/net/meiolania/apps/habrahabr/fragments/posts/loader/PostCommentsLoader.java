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

package net.meiolania.apps.habrahabr.fragments.posts.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.meiolania.apps.habrahabr.data.CommentsData;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class PostCommentsLoader extends AsyncTaskLoader<ArrayList<CommentsData>>{
	private String url;
	private ArrayList<CommentsData> commentsDatas = new ArrayList<CommentsData>();

	// TODO: rewrite
	private Hashtable<String, Boolean> containedComments = new Hashtable<String, Boolean>();

	public PostCommentsLoader(Context context, String url){
		super(context);

		this.url = url;
	}

	@Override
	public ArrayList<CommentsData> loadInBackground(){
		try{
			Document document = Jsoup.connect(url).get();
			Elements comments = document.select("div.comment_item");
			parseComments(comments, 0);
		}
		catch(IOException e){
		}

		return commentsDatas;
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
			// Element score = comment.select("span.score").first();

			// commentsData.setScore(score);
			commentsData.setUrl(linkToComment.attr("abs:href"));
			commentsData.setAuthorUrl(name.attr("abs:href"));
			commentsData.setAuthor(name.text());
			commentsData.setComment(message.text());
			commentsData.setLevel(level);

			commentsDatas.add(commentsData);

			Elements replyComments = comment.select("div.reply_comments > div.comment_item");
			
			parseComments(replyComments, level + 1);
		}
	}

}