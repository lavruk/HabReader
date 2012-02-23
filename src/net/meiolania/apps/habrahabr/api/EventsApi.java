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

import net.meiolania.apps.habrahabr.ui.events.EventsData;

public class EventsApi{
    protected static EventsApi eventsApiInstance = null;

    protected EventsApi(){
    }

    public static EventsApi getInstance(){
        if(eventsApiInstance == null)
            return(eventsApiInstance = new EventsApi());
        else
            return eventsApiInstance;
    }

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