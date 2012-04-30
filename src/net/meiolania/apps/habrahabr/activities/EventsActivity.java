package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.ComingEventFragment;
import net.meiolania.apps.habrahabr.fragments.CurrentEventFragment;
import net.meiolania.apps.habrahabr.fragments.PastEventFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class EventsActivity extends SherlockFragmentActivity implements TabListener{
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        showActionBar();
    }
    
    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.events);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        Preferences preferences = Preferences.getInstance(this);
        int selectedTab = preferences.getEventsDefaultTab();
        
        Tab tab = actionBar.newTab().setText(R.string.coming).setTag("coming").setTabListener(this);
        actionBar.addTab(tab, (selectedTab == 0 ? true : false));
        
        tab = actionBar.newTab().setText(R.string.current).setTag("current").setTabListener(this);
        actionBar.addTab(tab, (selectedTab == 1 ? true : false));
        
        tab = actionBar.newTab().setText(R.string.past).setTag("past").setTabListener(this);
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
        if(tab.getTag().equals("coming")){
            ComingEventFragment comingEventFragment = new ComingEventFragment();
            ft.replace(android.R.id.content, comingEventFragment);
        }else if(tab.getTag().equals("current")){
            CurrentEventFragment currentEventFragment = new CurrentEventFragment();
            ft.replace(android.R.id.content, currentEventFragment);
        }else if(tab.getTag().equals("past")){
            PastEventFragment pastEventFragment = new PastEventFragment();
            ft.replace(android.R.id.content, pastEventFragment);
        }
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft){
        
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft){
        
    }
    
}