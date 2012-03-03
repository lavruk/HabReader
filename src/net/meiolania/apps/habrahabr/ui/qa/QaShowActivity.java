/*
   Copyright (C) 2011 Andrey Zaytsev <a.einsam@gmail.com>
  
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
  
        http://www.apache.org/licenses/LICENSE-2.0
  
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package net.meiolania.apps.habrahabr.ui.qa;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.actions.HomeAction;
import net.meiolania.apps.habrahabr.ui.activities.ApplicationFragmentActivity;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class QaShowActivity extends ApplicationFragmentActivity{
    private String link;
    private QaShowFragment qaShowFragment;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qa_show_activity);
        
        setActionBar();
        
        Bundle extras = getIntent().getExtras();
        link = extras.getString("link");
        
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        
        qaShowFragment = new QaShowFragment();
        qaShowFragment.setLink(link);
        qaShowFragment.setIsFullView(true);
        
        fragmentTransaction.add(R.id.qa_show_fragment, qaShowFragment);
        
        if(UIUtils.isTablet(this) || preferences.isUseTabletDesign()){
            QaCommentsFragment qaCommentsFragment = new QaCommentsFragment();
            qaCommentsFragment.setLink(link);
            
            fragmentTransaction.add(R.id.qa_show_comments, qaCommentsFragment);
        }else{
            FrameLayout postsShowCommentsLayout = (FrameLayout)findViewById(R.id.qa_show_comments);
            postsShowCommentsLayout.setVisibility(View.GONE);
        }
        
        fragmentTransaction.commit();
    }
    
    private void setActionBar(){
        if(!UIUtils.isHoneycombOrHigher()){
            ActionBar actionBar = (ActionBar)findViewById(R.id.actionbar);
            actionBar.setTitle(R.string.qa);
            actionBar.setHomeAction(new HomeAction(this, preferences));
            actionBar.addAction(new ShowCommentsAction());
        }else{
            ActionBar actionBarView = (ActionBar) findViewById(R.id.actionbar);
            actionBarView.setVisibility(View.GONE);
            
            android.app.ActionBar actionBar = getActionBar();
            actionBar.setTitle(R.string.qa);
            
            actionBar.setDisplayHomeAsUpEnabled(true);
            if(UIUtils.isIceCreamOrHigher())
                actionBar.setHomeButtonEnabled(true);
        }
    }
    
    @Override
    protected Intent getActionBarIntent(){
        final Intent intent = new Intent(this, QaActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
    
    private class ShowCommentsAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_comments;
        }

        public void performAction(View view){
            startCommentsActivity();
        }

    }
    
    private void startCommentsActivity(){
        Intent intent = new Intent(this, QaCommentsActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }
    
}