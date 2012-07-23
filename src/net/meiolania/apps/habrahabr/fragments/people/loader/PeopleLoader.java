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

package net.meiolania.apps.habrahabr.fragments.people.loader;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.data.PeopleData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class PeopleLoader extends AsyncTaskLoader<ArrayList<PeopleData>>{
	public final static String TAG = PeopleLoader.class.getName();
	public final static String DEFAULT_URL = "http://habrahabr.ru/people/";
	
	private String url;

	public PeopleLoader(Context context){
		super(context);

		url = DEFAULT_URL;
	}

	public PeopleLoader(Context context, String url){
		super(context);

		this.url = url;
	}

	@Override
	public ArrayList<PeopleData> loadInBackground(){
		ArrayList<PeopleData> data = new ArrayList<PeopleData>();

		try{
			Log.i(TAG, "Loading a page: " + url);

			Document document = Jsoup.connect(url).get();
			Elements users = document.select("div.user");

			for(Element user : users){
				PeopleData peopleData = new PeopleData();

				Element rating = user.select("div.rating").first();
				Element karma = user.select("div.karma").first();
				Element avatar = user.select("div.avatar > a > img").first();
				Element name = user.select("div.userlogin > div.username > a").first();
				Element lifetime = user.select("div.info > div.lifetime").first();

				peopleData.setName(name.text());
				peopleData.setUrl(name.attr("abs:href"));
				peopleData.setRating(rating.text());
				peopleData.setKarma(karma.text());
				peopleData.setAvatar(avatar.attr("src"));
				peopleData.setLifetime(lifetime.text());

				data.add(peopleData);
			}
		}
		catch(IOException e){
		}

		return data;
	}

}