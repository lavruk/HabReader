package net.meiolania.apps.habrahabr.ui.activities;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.actions.HomeAction;
import net.meiolania.apps.habrahabr.ui.fragments.PeopleShowFragment;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.markupartist.android.widget.ActionBar;

public class PeopleShowActivity extends ApplicationFragmentActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_show_activity);
        
        Bundle extras = getIntent().getExtras();
        String link = extras.getString("link");
        
        setActionBar();
        
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        
        PeopleShowFragment peopleShowFragment = new PeopleShowFragment();
        peopleShowFragment.setLink(link);
        
        fragmentTransaction.add(R.id.people_show_fragment, peopleShowFragment);
        
        fragmentTransaction.commit();
    }
    
    private void setActionBar(){
        if(!UIUtils.isHoneycombOrHigher()){
            ActionBar actionBar = (ActionBar)findViewById(R.id.actionbar);
            actionBar.setTitle(R.string.people);
            actionBar.setHomeAction(new HomeAction(this));
        }else{
            ActionBar actionBarView = (ActionBar) findViewById(R.id.actionbar);
            actionBarView.setVisibility(View.GONE);
            
            android.app.ActionBar actionBar = getActionBar();
            actionBar.setTitle(R.string.people);
            
            if(UIUtils.isIceCreamOrHigher())
                actionBar.setHomeButtonEnabled(true);
        }
    }
    
}