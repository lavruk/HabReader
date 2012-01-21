package net.meiolania.apps.habrahabr.api;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.data.PostsData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PostsApi{

    public ArrayList<PostsData> getPosts(String link) throws IOException{
        ArrayList<PostsData> postsDataList = new ArrayList<PostsData>();

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
        
        return postsDataList;
    }

}