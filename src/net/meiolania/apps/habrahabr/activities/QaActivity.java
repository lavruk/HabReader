package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.HotQaFragment;
import net.meiolania.apps.habrahabr.fragments.InboxQaFragment;
import net.meiolania.apps.habrahabr.fragments.PopularQaFragment;
import net.meiolania.apps.habrahabr.fragments.UnansweredQaFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class QaActivity extends SherlockFragmentActivity implements TabListener{
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        showActionBar();
    }
    
    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.qa);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        Tab tab = actionBar.newTab().setText(R.string.inbox).setTag("inbox").setTabListener(this);
        actionBar.addTab(tab);
        
        tab = actionBar.newTab().setText(R.string.hot).setTag("hot").setTabListener(this);
        actionBar.addTab(tab);
        
        tab = actionBar.newTab().setText(R.string.popular).setTag("popular").setTabListener(this);
        actionBar.addTab(tab);
        
        tab = actionBar.newTab().setText(R.string.unanswered).setTag("unanswered").setTabListener(this);
        actionBar.addTab(tab);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.qa_activity, menu);
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
        if(tab.getTag().equals("inbox")){
            InboxQaFragment inboxQaFragment = new InboxQaFragment();
            ft.replace(android.R.id.content, inboxQaFragment);
        }else if(tab.getTag().equals("hot")){
            HotQaFragment hotQaFragment = new HotQaFragment();
            ft.replace(android.R.id.content, hotQaFragment);
        }else if(tab.getTag().equals("popular")){
            PopularQaFragment popularQaFragment = new PopularQaFragment();
            ft.replace(android.R.id.content, popularQaFragment);
        }else if(tab.getTag().equals("unanswered")){
            UnansweredQaFragment unansweredQaFragment = new UnansweredQaFragment();
            ft.replace(android.R.id.content, unansweredQaFragment);
        }
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft){
        
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft){
        
    }
    
}