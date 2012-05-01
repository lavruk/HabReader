package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.BestPostsFragment;
import net.meiolania.apps.habrahabr.fragments.CorporatePostsFragment;
import net.meiolania.apps.habrahabr.fragments.ThematicPostsFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class PostsActivity extends SherlockFragmentActivity implements TabListener{
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        showActionBar();
    }

    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.posts);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        Preferences preferences = Preferences.getInstance(this);
        int selectedTab = preferences.getPostsDefaultTab();

        Tab tab = actionBar.newTab().setText(R.string.best).setTag("best").setTabListener(this);
        actionBar.addTab(tab, (selectedTab == 0 ? true : false));
        
        tab = actionBar.newTab().setText(R.string.thematic).setTag("thematic").setTabListener(this);
        actionBar.addTab(tab, (selectedTab == 1 ? true : false));
        
        tab = actionBar.newTab().setText(R.string.corporate).setTag("corporate").setTabListener(this);
        actionBar.addTab(tab, (selectedTab == 2 ? true : false));
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