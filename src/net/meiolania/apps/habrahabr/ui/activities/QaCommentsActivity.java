package net.meiolania.apps.habrahabr.ui.activities;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.actions.HomeAction;
import net.meiolania.apps.habrahabr.ui.fragments.QaShowFragment;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.markupartist.android.widget.ActionBar;

public class QaCommentsActivity extends ApplicationFragmentActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qa_comments_activity);
        
        setActionBar();
        
        Bundle extras = getIntent().getExtras();
        String link = extras.getString("link");
        
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        QaShowFragment qaShowFragment = new QaShowFragment();
        qaShowFragment.setLink(link);
        fragmentTransaction.add(R.id.qa_show_comments, qaShowFragment);

        fragmentTransaction.commit();
    }
    
    private void setActionBar(){
        if(!UIUtils.isHoneycombOrHigher()){
            ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
            actionBar.setTitle(R.string.comments);
            actionBar.setHomeAction(new HomeAction(this));
        }else{
            ActionBar actionBarView = (ActionBar) findViewById(R.id.actionbar);
            actionBarView.setVisibility(View.GONE);

            android.app.ActionBar actionBar = getActionBar();
            actionBar.setTitle(R.string.comments);

            if(UIUtils.isIceCreamOrHigher())
                actionBar.setHomeButtonEnabled(true);
        }
    }
    
}