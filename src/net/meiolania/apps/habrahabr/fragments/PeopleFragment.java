package net.meiolania.apps.habrahabr.fragments;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.PeopleShowActivity;
import net.meiolania.apps.habrahabr.adapters.PeopleAdapter;
import net.meiolania.apps.habrahabr.data.PeopleData;
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
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;

public class PeopleFragment extends SherlockListFragment implements OnScrollListener{
    public final static String LOG_TAG = "PeopleFragment";
    public final static String URL = "http://habrahabr.ru/people/page%d/";
    protected final ArrayList<PeopleData> peopleDatas = new ArrayList<PeopleData>();
    protected boolean loadMoreData = true;
    protected PeopleAdapter peopleAdapter;
    protected int page = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        peopleAdapter = new PeopleAdapter(getSherlockActivity(), peopleDatas);
        setListAdapter(peopleAdapter);
        getListView().setOnScrollListener(this);
    }

    protected void loadList(){
        ++page;
        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
        new LoadPeople().execute();
    }

    protected final class LoadPeople extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                Log.i(LOG_TAG, "Loading " + String.format(URL, page));

                Document document = Jsoup.connect(String.format(URL, page)).get();
                Elements users = document.select("div.user");
                for(Element user : users){
                    PeopleData peopleData = new PeopleData();
                    
                    Element rating = user.select("div.rating").first();
                    Element karma = user.select("div.karma").first();
                    //TODO: null pointer exception. Need to fix.(Below)
                    Element avatar = user.select("div.avatar > img").first();
                    Element name = user.select("div.info > div.username > a").first();
                    Element lifetime = user.select("div.info > div.lifetime").first();
                    
                    peopleData.setName(name.text());
                    peopleData.setUrl(name.attr("abs:href"));
                    peopleData.setRating(rating.text());
                    peopleData.setKarma(karma.text());
                    //peopleData.setAvatar(avatar.attr("src"));
                    peopleData.setLifetime(lifetime.text());
                    
                    peopleDatas.add(peopleData);
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
                        peopleAdapter.notifyDataSetChanged();
                    loadMoreData = true;
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                }
            });
        }

    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        showUser(position);
    }

    protected void showUser(int position){
        PeopleData peopleData = peopleDatas.get(position);
        Intent intent = new Intent(getSherlockActivity(), PeopleShowActivity.class);
        intent.putExtra(PeopleShowActivity.EXTRA_NAME, peopleData.getName());
        intent.putExtra(PeopleShowActivity.EXTRA_URL, peopleData.getUrl());
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