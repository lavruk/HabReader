package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.BestPostsFragment;
import net.meiolania.apps.habrahabr.fragments.CorporatePostsFragment;
import net.meiolania.apps.habrahabr.fragments.ThematicPostsFragment;
import net.meiolania.apps.habrahabr.ui.TabManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class PostsActivity extends SherlockFragmentActivity{
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
        showActionBar();
        setupTabs();
    }
    
    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.posts);
    }
    
    private void setupTabs(){
        final TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        
        final TabManager tabManager = new TabManager(this, tabHost, R.id.realtabcontent);
        tabManager.addTab(tabHost.newTabSpec("best").setIndicator(getString(R.string.best)), BestPostsFragment.class, null);
        tabManager.addTab(tabHost.newTabSpec("thematic").setIndicator(getString(R.string.thematic)), ThematicPostsFragment.class, null);
        tabManager.addTab(tabHost.newTabSpec("corporate").setIndicator(getString(R.string.corporate)), CorporatePostsFragment.class, null);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
}