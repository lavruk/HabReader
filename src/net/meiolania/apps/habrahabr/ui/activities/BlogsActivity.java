package net.meiolania.apps.habrahabr.ui.activities;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.actions.HomeAction;
import net.meiolania.apps.habrahabr.ui.fragments.BlogsFragment;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.markupartist.android.widget.ActionBar;

public class BlogsActivity extends ApplicationFragmentActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blogs_activity);

        setActionBar();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        BlogsFragment blogsFragment = new BlogsFragment();

        fragmentTransaction.add(R.id.blogs_list_fragment, blogsFragment);

        if(!UIUtils.isTablet(this) && !preferences.isUseTabletDesign()){
            FrameLayout postsFrameLayout = (FrameLayout) findViewById(R.id.posts_list_fragment);
            postsFrameLayout.setVisibility(View.GONE);
        }

        fragmentTransaction.commit();
    }

    private void setActionBar(){
        if(!UIUtils.isHoneycombOrHigher()){
            ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
            actionBar.setTitle(R.string.blogs);
            actionBar.setHomeAction(new HomeAction(this));
        }else{
            ActionBar actionBarView = (ActionBar) findViewById(R.id.actionbar);
            actionBarView.setVisibility(View.GONE);

            android.app.ActionBar actionBar = getActionBar();
            actionBar.setTitle(R.string.blogs);

            if(UIUtils.isIceCreamOrHigher())
                actionBar.setHomeButtonEnabled(true);
        }
    }

}