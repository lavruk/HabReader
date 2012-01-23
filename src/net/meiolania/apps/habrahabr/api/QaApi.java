package net.meiolania.apps.habrahabr.api;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.meiolania.apps.habrahabr.data.QAData;

public class QaApi{
    
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