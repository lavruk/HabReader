package net.meiolania.apps.habrahabr.ui.activities;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.actions.HomeAction;
import net.meiolania.apps.habrahabr.ui.fragments.QaFragment;
import net.meiolania.apps.habrahabr.ui.fragments.QaShowFragment;
import net.meiolania.apps.habrahabr.utils.UIUtils;

import com.markupartist.android.widget.ActionBar;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

public class QaActivity extends ApplicationFragmentActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qa_activity);
        
        setActionBar();
        
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        
        QaFragment qaFragment = new QaFragment();
        
        fragmentTransaction.add(R.id.qa_list_fragment, qaFragment);
        
        if(UIUtils.isTablet(this) || preferences.isUseTabletDesign()){
            QaShowFragment qaShowFragment = new QaShowFragment();
            fragmentTransaction.add(R.id.qa_show_fragment, qaShowFragment);
        }else{
            FrameLayout qaShowFrameLayout = (FrameLayout) findViewById(R.id.qa_show_fragment);
            qaShowFrameLayout.setVisibility(View.GONE);
        }
        
        fragmentTransaction.commit();
    }
    
    private void setActionBar(){
        if(!UIUtils.isHoneycombOrHigher()){
            ActionBar actionBar = (ActionBar)findViewById(R.id.actionbar);
            actionBar.setTitle(R.string.qa);
            actionBar.setHomeAction(new HomeAction(this));
        }else{
            ActionBar actionBarView = (ActionBar) findViewById(R.id.actionbar);
            actionBarView.setVisibility(View.GONE);
            
            android.app.ActionBar actionBar = getActionBar();
            actionBar.setTitle(R.string.qa);
            
            if(UIUtils.isIceCreamOrHigher())
                actionBar.setHomeButtonEnabled(true);
        }
    }
    
}