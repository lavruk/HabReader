package net.meiolania.apps.habrahabr.api;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.meiolania.apps.habrahabr.data.BlogsData;

public class BlogsApi{
    
    public ArrayList<BlogsData> getBlogs(int page) throws IOException{
        ArrayList<BlogsData> blogsDataList = new ArrayList<BlogsData>();
        
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
        
        return blogsDataList;
    }
    
}