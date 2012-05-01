package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.fragments.HubsPostsFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;

public class HubsShowActivity extends AbstractionActivity{
    public final static String EXTRA_URL = "url";
    public final static String EXTRA_TITLE = "title";
    protected String url;
    protected String title;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        loadExtras();
        showActionBar();
        loadHubsPosts();
    }
    
    private void loadExtras(){
        url = getIntent().getStringExtra(EXTRA_URL);
        title = getIntent().getStringExtra(EXTRA_TITLE);
    }
    
    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
    }
    
    private void loadHubsPosts(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        HubsPostsFragment hubsPostsFragment = new HubsPostsFragment(url);
        fragmentTransaction.replace(android.R.id.content, hubsPostsFragment);
        fragmentTransaction.commit();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, HubsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
}