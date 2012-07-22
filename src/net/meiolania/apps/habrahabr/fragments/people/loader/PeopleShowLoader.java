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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import net.meiolania.apps.habrahabr.data.PeopleFullData;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class PeopleShowLoader extends AsyncTaskLoader<PeopleFullData>{
	public final static String TAG = PeopleShowLoader.class.getName();
	private String url;

	public PeopleShowLoader(Context context, String url){
		super(context);

		this.url = url;
	}

	@Override
	public PeopleFullData loadInBackground(){
		PeopleFullData peopleFullData = new PeopleFullData();

		try{
			Log.i(TAG, "Loading a page: " + url);

			Document document = Jsoup.connect(url).get();
			Element avatar = document.select("a.avatar > img").first();
			Element karma = document.select("div.karma > div.score > div.num").first();
			Element rating = document.select("div.rating > div.num").first();
			Element birthday = document.select("dd.bday").first();
			Element fullname = document.select("div.fullname").first();
			Element summary = document.select("dd.summary").first();
			Element interests = document.select("dl.interests > dd").first();

			peopleFullData.setAvatar(avatar.attr("src"));
			peopleFullData.setKarma(karma.text());
			peopleFullData.setRating(rating.text());
			peopleFullData.setBirthday(birthday != null ? birthday.text() : "");
			peopleFullData.setFullname(fullname != null ? fullname.text() : "");
			peopleFullData.setSummary(summary != null ? summary.text() : "");
			peopleFullData.setInterests(interests != null ? interests.text() : "");
		}
		catch(IOException e){
		}

		return peopleFullData;
	}

}