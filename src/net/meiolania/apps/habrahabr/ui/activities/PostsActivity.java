package net.meiolania.apps.habrahabr.ui.activities;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.fragments.PostsFragment;
import net.meiolania.apps.habrahabr.ui.fragments.PostsShowFragment;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

public class PostsActivity extends ApplicationFragmentActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_activity);
        
        setActionBar();
        
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        
        PostsFragment postsFragment = new PostsFragment();
        
        fragmentTransaction.add(R.id.posts_list_fragment, postsFragment);
        
        if(UIUtils.isTablet(this) || preferences.isUseTabletDesign()){
            PostsShowFragment postsShowFragment = new PostsShowFragment();
            fragmentTransaction.add(R.id.post_show_fragment, postsShowFragment);
        }else{
            FrameLayout postsShowFrameLayout = (FrameLayout)findViewById(R.id.post_show_fragment);
            postsShowFrameLayout.setVisibility(View.GONE);
        }
        
        fragmentTransaction.commit();
    }
    
    private void setActionBar(){
        
    }
    
}