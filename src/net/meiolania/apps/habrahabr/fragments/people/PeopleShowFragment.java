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

package net.meiolania.apps.habrahabr.fragments.people;

import java.io.IOException;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.PeopleFullData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PeopleShowFragment extends SherlockFragment{
    public final static String LOG_TAG = "PeopleShowFragment";
    protected String url;

    public PeopleShowFragment(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        loadInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.people_show_activity, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.people_show_activity, menu);
    }

    protected void loadInfo(){
        new LoadPeopleInfo().execute();
    }

    protected final class LoadPeopleInfo extends AsyncTask<Void, Void, PeopleFullData>{
        private ProgressDialog progressDialog;

        @Override
        protected PeopleFullData doInBackground(Void... params){
            PeopleFullData peopleFullData = new PeopleFullData();
            try{
                Log.i(LOG_TAG, "Loading " + url);

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

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(getSherlockActivity());
            progressDialog.setTitle(R.string.loading);
            progressDialog.setMessage(getString(R.string.loading_profile_info));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(final PeopleFullData result){
            getSherlockActivity().runOnUiThread(new Runnable(){
                public void run(){
                    if(!isCancelled()){
                        SherlockFragmentActivity activity = getSherlockActivity();
                        
                        ImageView avatar = (ImageView)activity.findViewById(R.id.avatar);
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.init(ImageLoaderConfiguration.createDefault(getSherlockActivity()));
                        imageLoader.displayImage(result.getAvatar(), avatar);
                        
                        TextView fullname = (TextView) activity.findViewById(R.id.fullname);
                        fullname.setText(result.getFullname());
                        if(result.getFullname().length() <= 0)
                            fullname.setVisibility(View.GONE);

                        TextView karma = (TextView) activity.findViewById(R.id.karma);
                        karma.setText(result.getKarma());

                        TextView rating = (TextView) activity.findViewById(R.id.rating);
                        rating.setText(result.getRating());

                        TextView birthday = (TextView) activity.findViewById(R.id.birthday);
                        birthday.setText(result.getBirthday());
                        if(result.getBirthday().length() <= 0)
                            birthday.setVisibility(View.GONE);

                        TextView interests = (TextView) activity.findViewById(R.id.interests);
                        interests.setText(result.getInterests());
                        if(result.getInterests().length() <= 0)
                            interests.setVisibility(View.GONE);

                        TextView summary = (TextView) activity.findViewById(R.id.summary);
                        summary.setText(result.getSummary());
                        if(result.getSummary().length() <= 0)
                            summary.setVisibility(View.GONE);
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }

}