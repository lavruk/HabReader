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

import net.meiolania.apps.habrahabr.ui.hubs.HubsData;

public class HubsApi{
    protected static HubsApi blogsApiInstance = null;

    protected HubsApi(){
    }

    public static HubsApi getInstance(){
        if(blogsApiInstance == null)
            return(blogsApiInstance = new HubsApi());
        else
            return blogsApiInstance;
    }

    public ArrayList<HubsData> getBlogs(ArrayList<HubsData> hubsDataList, int page) throws IOException{
        Document document = Jsoup.connect("http://habrahabr.ru/hubs/page" + page + "/").get();
        Elements hubs = document.select("div.hub");
        
        for(Element hub : hubs){
            HubsData hubsData = new HubsData();
            
            Element link = hub.select("div.title > a").first();
            Element stat = hub.select("div.stat").first();
            Element profileHub = hub.select("span.profiled_hub").first();
            
            StringBuilder title = new StringBuilder(link.text());
            if(profileHub != null)
                title.append(" *");
            
            hubsData.setTitle(title.toString());
            hubsData.setLink(link.attr("abs:href"));
            hubsData.setStatistics(stat.text());
            
            hubsDataList.add(hubsData);
        }

        return hubsDataList;
    }

}