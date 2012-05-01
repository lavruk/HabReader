package net.meiolania.apps.habrahabr.activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.PeopleFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;

public class PeopleSearchActivity extends AbstractionActivity{
    public final static String URL = "http://habrahabr.ru/search/page%page%/?target_type=users&order_by=relevance&q=%query%";
    public final static String EXTRA_QUERY = "query";
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        loadExtras();
        showActionBar();
        loadSearchedPeople();
    }

    private void loadExtras(){
        query = getIntent().getStringExtra(EXTRA_QUERY);
    }

    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.people_search);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void loadSearchedPeople(){
        PeopleFragment peopleFragment = new PeopleFragment(getUrl());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, peopleFragment);
        fragmentTransaction.commit();
    }

    private String getUrl(){
        try{
            return URL.replace("%query%", URLEncoder.encode(query, "UTF-8"));
        }
        catch(UnsupportedEncodingException e){
        }
        return URL.replace("%query%", query);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, PeopleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}