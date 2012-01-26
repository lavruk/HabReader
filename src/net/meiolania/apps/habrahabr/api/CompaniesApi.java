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