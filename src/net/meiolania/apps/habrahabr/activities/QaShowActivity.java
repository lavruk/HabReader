package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.R;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;

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

    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft){

    }

    public void onTabReselected(Tab tab, FragmentTransaction ft){

    }

}