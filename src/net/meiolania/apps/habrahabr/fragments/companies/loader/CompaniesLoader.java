/*
Copyright 2012 Andrey Zaytsev

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

package net.meiolania.apps.habrahabr.fragments.companies.loader;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.data.CompaniesData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class CompaniesLoader extends AsyncTaskLoader<ArrayList<CompaniesData>>
{
	public final static String TAG = CompaniesLoader.class.getName();
	public final static String URL = "http://habrahabr.ru/companies/page%d/";
	private static int page;

	public CompaniesLoader(Context context)
	{
		super(context);
	}

	public static void setPage(int page)
	{
		CompaniesLoader.page = page;
	}

	@Override
	public ArrayList<CompaniesData> loadInBackground()
	{
		ArrayList<CompaniesData> data = new ArrayList<CompaniesData>();

		try
		{
			String readyUrl = String.format(URL, page);

			Log.i(TAG, "Loading a page: " + readyUrl);

			Document document = Jsoup.connect(readyUrl).get();

			Elements companies = document.select("div.company");

			for(Element company : companies)
			{
				CompaniesData companiesData = new CompaniesData();

				Element icon = company.select("div.icon > img").first();
				Element index = company.select("div.habraindex").first();
				Element title = company.select("div.description > div.name > a").first();
				Element description = company.select("div.description > p").first();

				companiesData.setTitle(title.text());
				companiesData.setUrl(title.attr("abs:href"));
				// TODO: rewrite the code below
				companiesData.setIcon("http://habrahabr.ru" + icon.attr("src"));
				companiesData.setIndex(index.text());
				companiesData.setDescription(description.text());

				data.add(companiesData);
			}
		}
		catch(IOException e)
		{
		}

		return data;
	}

}