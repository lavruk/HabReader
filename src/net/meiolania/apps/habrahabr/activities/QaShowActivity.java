package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.QaShowFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class QaShowActivity extends SherlockFragmentActivity implements TabListener{
    public final static String EXTRA_URL = "url";
    public final static String EXTRA_TITLE = "title";
    private String title;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        loadExtras();
        showActionBar();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, QaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadExtras(){
        url = getIntent().getStringExtra(EXTRA_URL);
        title = getIntent().getStringExtra(EXTRA_TITLE);
    }

    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Tab tab = actionBar.newTab().setText(R.string.question).setTag("question").setTabListener(this);
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText(R.string.comments).setTag("comments").setTabListener(this);
        actionBar.addTab(tab);
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft){
        if(tab.getTag().equals("question")){
            QaShowFragment qaShowFragment = new QaShowFragment(url);
            ft.replace(android.R.id.content, qaShowFragment);
        }else if(tab.getTag().equals("comments")){
            
        }
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft){

    }

    public void onTabReselected(Tab tab, FragmentTransaction ft){

    }

}