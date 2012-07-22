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

package net.meiolania.apps.habrahabr.fragments.events.loader;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.meiolania.apps.habrahabr.data.EventFullData;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class EventShowLoader extends AsyncTaskLoader<EventFullData>{
	public final static int INFO_LOCATION = 0;
	public final static int INFO_PAY = 1;
	public final static int INFO_SITE = 2;
	private String url;

	public EventShowLoader(Context context, String url){
		super(context);

		this.url = url;
	}

	@Override
	public EventFullData loadInBackground(){
		EventFullData event = new EventFullData();

		try{
			Document document = Jsoup.connect(url).get();

			Element day = document.select("div.date > div.day").first();
			Element month = document.select("div.date > div.month").first();
			// TODO: add logo
			Element logo = document.select("div.logo > img").first();
			Element title = document.select("div.info_block > h1.title").first();
			Element detail = document.select("div.info_block > div.detail").first();
			Element description = document.select("div.info_block > div.description").first();
			Elements additionalInfo = document.select("div.info_block").first().getElementsByTag("dd");

			Element location = null, pay = null, site = null;
			// TODO: need to test this code more
			int i = 0;
			for(Element info : additionalInfo){
				switch(i){
					case INFO_LOCATION:
						location = info;
						break;
					case INFO_PAY:
						pay = info;
						break;
					case INFO_SITE:
						site = info;
						break;
				}
				i++;
			}

			event.setTitle(title.text());
			event.setUrl(url);
			event.setDate(day.text() + " " + month.text());
			// eventsFullData.setLogo(logo.attr("src"));
			event.setDetail(detail.text());
			event.setText(description.text());
			event.setPay(pay.text());
			event.setLocation(location.text());
			event.setSite(site.text());
		}
		catch(IOException e){
		}

		return event;
	}

}