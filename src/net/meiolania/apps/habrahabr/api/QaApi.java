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

import net.meiolania.apps.habrahabr.data.QAData;

public class QaApi{
    protected static QaApi qaApiInstance = null;
    
    protected QaApi(){}
    
    public static QaApi getInstance(){
        if(qaApiInstance == null)
            return (qaApiInstance = new QaApi());
        else
            return qaApiInstance;
    }
    
    public void getQa(ArrayList<QAData> qaDataList, int page) throws IOException{
        Document document = Jsoup.connect("http://habrahabr.ru/qa/page" + page + "/").get();
        Elements qaList = document.select("div.post");

        for(Element question : qaList){
            QAData qaData = new QAData();

            Element link = question.select("a.post_title").first();
            qaData.setTitle(link.text());

            qaData.setLink(link.attr("abs:href"));

            Element tags = question.select("ul.tags").first();
            qaData.setTags(tags.text());

            qaDataList.add(qaData);
        }
    }
    
}