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
import net.meiolania.apps.habrahabr.adapters.QAAdapter;
import net.meiolania.apps.habrahabr.data.QAData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class QA extends ApplicationActivity{
    private final ArrayList<QAData> qaDataList = new ArrayList<QAData>();
    private QAAdapter qaAdapter;
    private int page;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qa);

        setActionBar();
        loadList();
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_SEARCH:
                startActivity(new Intent(this, QASearch.class));
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setActionBar(){
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.qa);
        actionBar.addAction(new LoadNextPageAction());
        actionBar.addAction(new UpdateAction());
        actionBar.addAction(new IntentAction(this, new Intent(this, QASearch.class), R.drawable.actionbar_ic_search));
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
        new LoadQAList().execute();
    }

    private class LoadQAList extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect("http://habrahabr.ru/qa/page" + page + "/").get();
                Elements qaList = document.select("div.hentry");
                
                for(Element question : qaList){
                    QAData qaData = new QAData();
                    
                    Element link = question.select("a.topic").first();
                    qaData.setTitle(link.text());
                    
                    qaData.setLink(link.attr("abs:href"));
                    
                    Element tags = question.select("ul.tags").first();
                    qaData.setTags(tags.text());
                    
                    qaDataList.add(qaData);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(QA.this);
            progressDialog.setMessage(getString(R.string.loading_qa_list));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled() && page == 1){
                qaAdapter = new QAAdapter(QA.this, qaDataList);
                
                ListView listView = (ListView)QA.this.findViewById(R.id.qa_list);
                listView.setAdapter(qaAdapter);
                listView.setOnItemClickListener(new OnItemClickListener(){
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        QAData qaData = qaDataList.get(position);
                        
                        Intent intent = new Intent(QA.this, QAShow.class);
                        intent.putExtra("link", qaData.getLink());
                        
                        QA.this.startActivity(intent);
                    }
                });
            }else
                qaAdapter.notifyDataSetChanged();
            
            progressDialog.dismiss();
        }

    }

}