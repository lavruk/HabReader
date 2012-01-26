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

import net.meiolania.apps.habrahabr.data.PeopleData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PeopleApi{

    public void getPeople(ArrayList<PeopleData> peopleDataList, int page) throws IOException{
        Document document = Jsoup.connect("http://habrahabr.ru/people/page" + page + "/").get();
        Element tableUsers = document.select("table.users-list").first();

        Elements dataUsers = tableUsers.getElementsByTag("tr");

        boolean firstElement = true;
        for(Element user : dataUsers){
            if(firstElement){
                firstElement = false;
                continue;
            }
            PeopleData peopleData = new PeopleData();

            Element userAvatar = user.getElementsByTag("img").first();
            Element userKarma = user.select("td.userkarma").first();
            Element userRating = user.select("td.userrating").first();
            Element userLink = user.select("div.habrauserava > a").first();

            peopleData.setName(userAvatar.attr("alt"));
            peopleData.setKarma(userKarma.text());
            peopleData.setRating(userRating.text());
            peopleData.setLink(userLink.attr("abs:href"));

            peopleDataList.add(peopleData);
        }
    }

}