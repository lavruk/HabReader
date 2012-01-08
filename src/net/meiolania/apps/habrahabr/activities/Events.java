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

package net.meiolania.apps.habrahabr.activities;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.EventsAdapter;
import net.meiolania.apps.habrahabr.data.EventsData;
import net.meiolania.apps.habrahabr.utils.VibrateUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class Events extends ApplicationActivity{
    private final ArrayList<EventsData> eventsDataList = new ArrayList<EventsData>();
    private EventsAdapter eventsAdapter;
    private int page;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

        setActionBar();
        loadList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(preferences.isVibrate())
            VibrateUtils.doVibrate(this);
        switch(item.getItemId()){
            case R.id.to_home:
                startActivity(new Intent(this, Dashboard.class));
                break;
        }
        return true;
    }

    private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.events);
        actionBar.addAction(new LoadNextPageAction());
        actionBar.addAction(new UpdateAction());
    }

    private class LoadNextPageAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_forward;
        }

        public void performAction(View view){
            loadList();
        }

    }

    private class UpdateAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_update;
        }

        public void performAction(View view){
            loadList();
        }

    }

    private void loadList(){
        ++page;
        new LoadEventsList().execute();
    }

    private class LoadEventsList extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect("http://habrahabr.ru/events/coming/page" + page + "/").get();
                Element eventsList = document.select("div.events_list").first();

                Elements events = eventsList.select("div.event");

                for(Element event : events){
                    EventsData eventsData = new EventsData();

                    Element title = event.getElementsByTag("a").first();
                    Element detail = event.select("div.text").first();

                    eventsData.setTitle(title.text());
                    eventsData.setDetail(detail.text());
                    eventsData.setLink(title.attr("abs:href"));

                    eventsDataList.add(eventsData);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(Events.this);
            progressDialog.setMessage(getString(R.string.loading_events_list));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                eventsAdapter = new EventsAdapter(Events.this, eventsDataList);

                ListView listView = (ListView) Events.this.findViewById(R.id.events_list);
                listView.setAdapter(eventsAdapter);
                listView.setOnItemClickListener(new OnItemClickListener(){
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                        EventsData eventsData = eventsDataList.get(position);

                        Intent intent = new Intent(Events.this, EventsShow.class);
                        intent.putExtra("link", eventsData.getLink());

                        Events.this.startActivity(intent);
                    }
                });
            }else
                eventsAdapter.notifyDataSetChanged();

            progressDialog.dismiss();
        }

    }

}