package net.meiolania.apps.habrahabr.fragments;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.QaShowActivity;
import net.meiolania.apps.habrahabr.adapters.QaAdapter;
import net.meiolania.apps.habrahabr.data.QaData;
import net.meiolania.apps.habrahabr.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public abstract class AbstractionQaFragment extends SherlockListFragment implements OnScrollListener{
    public final static String LOG_TAG = "QaFragment";
    protected final ArrayList<QaData> qaDatas = new ArrayList<QaData>();
    protected QaAdapter qaAdapter;
    protected boolean loadMoreData = true;
    protected int page = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        qaAdapter = new QaAdapter(getSherlockActivity(), qaDatas);
        setListAdapter(qaAdapter);
        getListView().setOnScrollListener(this);
    }

    protected void loadList(){
        ++page;
        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
        new LoadQa().execute();
    }

    protected abstract String getUrl();

    protected final class LoadQa extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                Log.i(LOG_TAG, "Loading " + String.format(getUrl(), page));

                Document document = Jsoup.connect(String.format(getUrl(), page)).get();
                Elements qaList = document.select("div.post");
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
        if((firstVisibleItem + visibleItemCount) == totalItemCount && loadMoreData){
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