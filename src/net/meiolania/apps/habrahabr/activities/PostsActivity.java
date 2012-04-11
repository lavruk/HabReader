package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.BestPostsFragment;
import net.meiolania.apps.habrahabr.fragments.CorporatePostsFragment;
import net.meiolania.apps.habrahabr.fragments.ThematicPostsFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PostsActivity extends SherlockFragmentActivity implements TabListener{
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        showActionBar();
    }

    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.posts);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Tab tab = actionBar.newTab().setText(R.string.best).setTag("best").setTabListener(this);
        actionBar.addTab(tab);
        
        tab = actionBar.newTab().setText(R.string.thematic).setTag("thematic").setTabListener(this);
        actionBar.addTab(tab);
        
        tab = actionBar.newTab().setText(R.string.corporate).setTag("corporate").setTabListener(this);
        actionBar.addTab(tab);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.posts_activity, menu);
        return super.onCreateOptionsMenu(menu);
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

    public void onTabSelected(Tab tab, FragmentTransaction ft){
        if(tab.getTag().equals("best")){
            BestPostsFragment bestPostsFragment = new BestPostsFragment();
            ft.replace(android.R.id.content, bestPostsFragment);
        }else if(tab.getTag().equals("thematic")){
            ThematicPostsFragment thematicPostsFragment = new ThematicPostsFragment();
            ft.replace(android.R.id.content, thematicPostsFragment);
        }else if(tab.getTag().equals("corporate")){
            CorporatePostsFragment corporatePostsFragment = new CorporatePostsFragment();
            ft.replace(android.R.id.content, corporatePostsFragment);
        }
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft){
        
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft){
        
    }

}