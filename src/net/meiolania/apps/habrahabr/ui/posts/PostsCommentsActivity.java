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

package net.meiolania.apps.habrahabr.ui.posts;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.actions.HomeAction;
import net.meiolania.apps.habrahabr.ui.activities.ApplicationFragmentActivity;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.markupartist.android.widget.ActionBar;

public class PostsCommentsActivity extends ApplicationFragmentActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_comments_activity);
        
        setActionBar();

        Bundle extras = getIntent().getExtras();
        String link = extras.getString("link");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        PostsCommentsFragment postsCommentsFragment = new PostsCommentsFragment();
        postsCommentsFragment.setLink(link);
        fragmentTransaction.add(R.id.post_show_comments, postsCommentsFragment);

        fragmentTransaction.commit();
    }

    private void setActionBar(){
        if(!UIUtils.isHoneycombOrHigher()){
            ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
            actionBar.setTitle(R.string.comments);
            actionBar.setHomeAction(new HomeAction(this, preferences));
        }else{
            ActionBar actionBarView = (ActionBar) findViewById(R.id.actionbar);
            actionBarView.setVisibility(View.GONE);

            android.app.ActionBar actionBar = getActionBar();
            actionBar.setTitle(R.string.comments);
            
            actionBar.setDisplayHomeAsUpEnabled(true);
            if(UIUtils.isIceCreamOrHigher())
                actionBar.setHomeButtonEnabled(true);
        }
    }
    
    @Override
    protected Intent getActionBarIntent(){
        return null;
    }

}