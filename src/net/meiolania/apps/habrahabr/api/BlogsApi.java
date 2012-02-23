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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.meiolania.apps.habrahabr.ui.blogs.BlogsData;

public class BlogsApi{
    protected static BlogsApi blogsApiInstance = null;

    protected BlogsApi(){
    }

    public static BlogsApi getInstance(){
        if(blogsApiInstance == null)
            return(blogsApiInstance = new BlogsApi());
        else
            return blogsApiInstance;
    }

    public ArrayList<BlogsData> getBlogs(ArrayList<BlogsData> blogsDataList, int page) throws IOException{
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