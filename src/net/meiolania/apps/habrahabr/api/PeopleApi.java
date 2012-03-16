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

import net.meiolania.apps.habrahabr.ui.people.PeopleData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PeopleApi{
    protected static PeopleApi peopleApiInstance = null;

    protected PeopleApi(){
    }

    public static PeopleApi getInstance(){
        if(peopleApiInstance == null)
            return(peopleApiInstance = new PeopleApi());
        else
            return peopleApiInstance;
    }

    public void getPeople(ArrayList<PeopleData> peopleDataList, int page) throws IOException{
        Document document = Jsoup.connect("http://habrahabr.ru/people/page" + page + "/").get();
        Elements users = document.select("div.user");
        
        for(Element user : users){
            PeopleData peopleData = new PeopleData();
            
            Element userLink = user.select("div.avatar > a").first();
            Element userAvatar = user.select("div.avatar > a > img").first();
            Element userRating = user.select("div.rating").first();
            Element userKarma = user.select("div.karma").first();
            Element userName = user.select("div.username > a").first();
            
            peopleData.setLink(userLink.attr("abs:href"));
            peopleData.setAvatar(userAvatar.attr("src"));
            peopleData.setRating(userRating.text());
            peopleData.setKarma(userKarma.text());
            peopleData.setName(userName.text());
            
            peopleDataList.add(peopleData);
        }
    }

}