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