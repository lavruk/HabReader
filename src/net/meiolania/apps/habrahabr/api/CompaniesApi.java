package net.meiolania.apps.habrahabr.api;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.meiolania.apps.habrahabr.data.CompaniesData;

public class CompaniesApi{
    
    public void getCompanies(ArrayList<CompaniesData> companiesDataList, int page) throws IOException{
        Document document = Jsoup.connect("http://habrahabr.ru/companies/page" + page + "/").get();
        Element tableCompanies = document.select("ul.corps-list").first();

        Elements dataCompanies = tableCompanies.select("li");

        boolean firstElement = true;
        for(Element company : dataCompanies){
            if(firstElement){
                firstElement = false;
                continue;
            }
            CompaniesData companiesData = new CompaniesData();

            Element logo = company.getElementsByTag("img").first();
            Element link = company.select("dt.corp-name > a").first();

            companiesData.setLink(link.attr("abs:href"));
            companiesData.setLogo(logo.attr("src"));
            companiesData.setTitle(logo.attr("title"));

            companiesDataList.add(companiesData);
        }
    }
    
}