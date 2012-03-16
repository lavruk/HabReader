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

import net.meiolania.apps.habrahabr.ui.companies.CompaniesData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CompaniesApi{
    protected static CompaniesApi companiesApiInstance = null;

    protected CompaniesApi(){
    }

    public static CompaniesApi getInstance(){
        if(companiesApiInstance == null)
            return(companiesApiInstance = new CompaniesApi());
        else
            return companiesApiInstance;
    }

    public void getCompanies(ArrayList<CompaniesData> companiesDataList, int page) throws IOException{
        Document document = Jsoup.connect("http://habrahabr.ru/companies/page" + page + "/").get();
        Elements companies = document.select("div.company");
        
        for(Element company : companies){
            CompaniesData companiesData = new CompaniesData();
            
            Element companyTitle = company.select("div.name > a").first();
            Element companyLogo = company.select("div.icon > img").first();
            
            companiesData.setLink(companyTitle.attr("abs:href"));
            companiesData.setTitle(companyTitle.text());
            companiesData.setLogo(companyLogo.attr("src"));
            
            companiesDataList.add(companiesData);
        }
    }

}