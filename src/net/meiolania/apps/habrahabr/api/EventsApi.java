package net.meiolania.apps.habrahabr.api;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.meiolania.apps.habrahabr.data.EventsData;

public class EventsApi{
    
    public void getEvents(ArrayList<EventsData> eventsDataList, int page) throws IOException{
        Document document = Jsoup.connect("http://habrahabr.ru/events/coming/page" + page + "/").get();
        Element eventsList = document.select("div.events_list").first();

        Elements events = eventsList.select("div.event");

        for(Element event : events){
            EventsData eventsData = new EventsData();

            Element title = event.select("h1.title > a").first();
            Element detail = event.select("div.text").first();

            eventsData.setTitle(title.text());
            eventsData.setDetail(detail.text());
            eventsData.setLink(title.attr("abs:href"));

            eventsDataList.add(eventsData);
        }
    }
    
}