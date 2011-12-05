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

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.adapters.PeopleAdapter;
import net.meiolania.apps.habrahabr.data.PeopleData;
import net.meiolania.apps.habrahabr.utils.Vibrate;

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

public class People extends ApplicationActivity{
    private final ArrayList<PeopleData> peopleDataList = new ArrayList<PeopleData>();
    private PeopleAdapter peopleAdapter;
    private int page;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people);

        setActionBar();
        loadList();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.people, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(Preferences.vibrate)
            Vibrate.doVibrate(this);
        switch(item.getItemId()){
            case R.id.to_home:
                startActivity(new Intent(this, Dashboard.class));
                break;
        }
        return true;
    }
    
    private void loadList(){
        ++page;
        new LoadPeopleList().execute();
    }

    private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.people);
        actionBar.addAction(new LoadNextPageAction());
    }
    
    private class LoadNextPageAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_forward;
        }

        public void performAction(View view){
            loadList();
        }
        
    }

    private class LoadPeopleList extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;
        
        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect("http://habrahabr.ru/people/page" + page + "/").get();
                Element tableUsers = document.select("table.users-list").first();
                
                Elements dataUsers = tableUsers.getElementsByTag("tr");
                
                boolean firstElement = true;
                for(Element user : dataUsers){
                    /*
                     * We need to skip the first <tr></tr> because it hasn`t data
                     */
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
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        
        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(People.this);
            progressDialog.setMessage(getString(R.string.loading_people_list));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
        
        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                peopleAdapter = new PeopleAdapter(People.this, peopleDataList);
                
                ListView listView = (ListView)People.this.findViewById(R.id.people_list);
                listView.setAdapter(peopleAdapter);
                listView.setOnItemClickListener(new OnItemClickListener(){
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                        PeopleData peopleData = peopleDataList.get(position);
                        
                        Intent intent = new Intent(People.this, PeopleShow.class);
                        intent.putExtra("link", peopleData.getLink());
                        
                        startActivity(intent);
                    }
                });
            }else
                peopleAdapter.notifyDataSetChanged();
            
            progressDialog.dismiss();
        }

    }

}