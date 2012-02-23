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

import net.meiolania.apps.habrahabr.ui.posts.PostsData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PostsApi{
    protected static PostsApi postsApiInstance = null;

    protected PostsApi(){
    }

    public static PostsApi getInstance(){
        if(postsApiInstance == null)
            return(postsApiInstance = new PostsApi());
        else
            return postsApiInstance;
    }

    public void getPosts(ArrayList<PostsData> postsDataList, String link) throws IOException{
        Document document = Jsoup.connect(link).get();

        Elements postsList = document.select("div.post");

        for(Element post : postsList){
            PostsData postsData = new PostsData();

            Element title = post.select("a.post_title").first();
            Element blog = post.select("a.blog_title").first();

            postsData.setTitle(title.text());
            postsData.setBlog((blog != null) ? blog.text() : "");

            postsData.setLink(title.attr("abs:href"));

            postsDataList.add(postsData);
        }
    }

}