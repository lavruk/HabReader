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

package net.meiolania.apps.habrahabr.fragments.qa;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.QaSearchActivity;
import net.meiolania.apps.habrahabr.activities.QaShowActivity;
import net.meiolania.apps.habrahabr.adapters.QaAdapter;
import net.meiolania.apps.habrahabr.data.QaData;
import net.meiolania.apps.habrahabr.utils.ConnectionUtils;
import net.meiolania.apps.habrahabr.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public abstract class AbstractionQaFragment extends SherlockListFragment implements OnScrollListener{
    public final static String LOG_TAG = "QaFragment";
    protected final ArrayList<QaData> qaDatas = new ArrayList<QaData>();
    protected QaAdapter qaAdapter;
    protected boolean loadMoreData = true;
    protected int page = 0;
    protected boolean noMorePages = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        
        qaAdapter = new QaAdapter(getSherlockActivity(), qaDatas);
        setListAdapter(qaAdapter);
        
        getListView().setOnScrollListener(this);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.qa_fragment, menu);
        
        final EditText searchQuery = (EditText) menu.findItem(R.id.search).getActionView().findViewById(R.id.search_query);
        searchQuery.setOnEditorActionListener(new OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    Intent intent = new Intent(getSherlockActivity(), QaSearchActivity.class);
                    intent.putExtra(QaSearchActivity.EXTRA_QUERY, searchQuery.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void loadList(){
    	if(ConnectionUtils.isConnected(getSherlockActivity())){
    		++page;
            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
            new LoadQa().execute();
    	}
    }

    protected abstract String getUrl();

    protected final class LoadQa extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                Log.i(LOG_TAG, "Loading " + getUrl().replace("%page%", String.valueOf(page)));

                Document document = Jsoup.connect(getUrl().replace("%page%", String.valueOf(page))).get();
                Elements qaList = document.select("div.post");
                
                if(qaList.size() <= 0){
                    noMorePages = true;
                    /*
                     * It's a solve for:
                     * java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
                     */
                    getSherlockActivity().runOnUiThread(new Runnable(){
                        public void run(){
                            Toast.makeText(getSherlockActivity(), R.string.no_more_pages, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                
                for(Element qa : qaList){
                    QaData qaData = new QaData();
                    
                    Element title = qa.select("a.post_title").first();
                    Element hubs = qa.select("div.hubs").first();
                    Element answers = qa.select("div.informative").first();
                    Element date = qa.select("div.published").first();
                    Element author = qa.select("div.author > a").first();
                    
                    qaData.setTitle(title.text());
                    qaData.setUrl(title.attr("abs:href"));
                    qaData.setHubs(hubs.text());
                    qaData.setAnswers(answers.text());
                    qaData.setDate(date.text());
                    qaData.setAuthor(author.text());
                    
                    qaDatas.add(qaData);
                }
            }
            catch(IOException e){
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            getSherlockActivity().runOnUiThread(new Runnable(){
                public void run(){
                    if(!isCancelled())
                        qaAdapter.notifyDataSetChanged();
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                    loadMoreData = true;
                }
            });
        }

    }
    
    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        showQa(position);
    }
    
    protected void showQa(int position){
        QaData qaData = qaDatas.get(position);
        Intent intent = new Intent(getSherlockActivity(), QaShowActivity.class);
        intent.putExtra(QaShowActivity.EXTRA_URL, qaData.getUrl());
        intent.putExtra(QaShowActivity.EXTRA_TITLE, qaData.getTitle());
        startActivity(intent);
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        if((firstVisibleItem + visibleItemCount) == totalItemCount && loadMoreData && !noMorePages){
            loadMoreData = false;
            loadList();
            Log.i(LOG_TAG, "Loading " + page + " page");
            //TODO: need to find a better way to display a notification for devices with Android < 3.0
            if(!UIUtils.isHoneycombOrHigher())
                Toast.makeText(getSherlockActivity(), getString(R.string.loading_page, page), Toast.LENGTH_SHORT).show();
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState){

    }

}