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

package net.meiolania.apps.habrahabr.api;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.ui.comments.CommentsData;
import net.meiolania.apps.habrahabr.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PostsCommentsApi{
    protected static PostsCommentsApi postsCommentsApiInstance = null;

    protected PostsCommentsApi(){
    }

    public static PostsCommentsApi getInstance(){
        if(postsCommentsApiInstance == null)
            return(postsCommentsApiInstance = new PostsCommentsApi());
        else
            return postsCommentsApiInstance;
    }

    public ArrayList<CommentsData> getComments(String link) throws IOException{
        ArrayList<CommentsData> commentsDataList = new ArrayList<CommentsData>();

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
            commentsData.setScore(UIUtils.parseCommentsScore(score.text()));

            commentsDataList.add(commentsData);
        }

        return commentsDataList;
    }

}