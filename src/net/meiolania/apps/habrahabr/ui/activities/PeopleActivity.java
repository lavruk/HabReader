package net.meiolania.apps.habrahabr.ui.activities;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.actions.HomeAction;
import net.meiolania.apps.habrahabr.ui.fragments.PeopleFragment;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.markupartist.android.widget.ActionBar;

public class PeopleActivity extends ApplicationFragmentActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_activity);
        
        setActionBar();
        
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        
        PeopleFragment peopleFragment = new PeopleFragment();
        fragmentTransaction.add(R.id.people_list_fragment, peopleFragment);
        
        if(!UIUtils.isTablet(this) && !preferences.isUseTabletDesign()){
            FrameLayout framePeopleShowLayout = (FrameLayout) findViewById(R.id.people_show_fragment);
            framePeopleShowLayout.setVisibility(View.GONE);
        }
        
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