package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.fragments.PeopleShowFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class PeopleShowActivity extends SherlockFragmentActivity{
    public final static String EXTRA_NAME = "name";
    public final static String EXTRA_URL = "url";
    private String name;
    private String url;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        loadExtras();
        showActionBar();
        loadInfo();
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
    
    private void loadExtras(){
        name = getIntent().getStringExtra(EXTRA_NAME);
        url = getIntent().getStringExtra(EXTRA_URL);
    }
    
    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(name);
    }
    
    private void loadInfo(){
        PeopleShowFragment peopleShowFragment = new PeopleShowFragment(url);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, peopleShowFragment);
        fragmentTransaction.commit();
    }
    
}