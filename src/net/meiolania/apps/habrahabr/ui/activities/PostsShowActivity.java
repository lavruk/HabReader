package net.meiolania.apps.habrahabr.ui.activities;

import java.util.Formatter;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.actions.HomeAction;
import net.meiolania.apps.habrahabr.ui.fragments.PostsCommentsFragment;
import net.meiolania.apps.habrahabr.ui.fragments.PostsShowFragment;
import net.meiolania.apps.habrahabr.utils.UIUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class PostsShowActivity extends ApplicationFragmentActivity{
    private String link;
    private PostsShowFragment postsShowFragment;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_show_activity);
        
        Bundle extras = getIntent().getExtras();
        link = extras.getString("link");
        
        setActionBar();
        
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        
        postsShowFragment = new PostsShowFragment();
        postsShowFragment.setLink(link);
        postsShowFragment.setIsFullView(true);
        
        fragmentTransaction.add(R.id.post_show_fragment, postsShowFragment);
        
        if(UIUtils.isTablet(this) || preferences.isUseTabletDesign()){
            PostsCommentsFragment postsCommentsFragment = new PostsCommentsFragment();
            postsCommentsFragment.setLink(link);
            
            fragmentTransaction.add(R.id.post_show_comments, postsCommentsFragment);
        }else{
            FrameLayout postsShowCommentsLayout = (FrameLayout)findViewById(R.id.post_show_comments);
            postsShowCommentsLayout.setVisibility(View.GONE);
        }
        
        fragmentTransaction.commit();
    }
    
    private void setActionBar(){
        if(!UIUtils.isHoneycombOrHigher()){
            ActionBar actionBar = (ActionBar)findViewById(R.id.actionbar);
            actionBar.setTitle(R.string.posts);
            actionBar.setHomeAction(new HomeAction(this));
            actionBar.addAction(new ShowCommentsAction());
            actionBar.addAction(new ShareAction());
        }else{
            ActionBar actionBarView = (ActionBar) findViewById(R.id.actionbar);
            actionBarView.setVisibility(View.GONE);
            
            android.app.ActionBar actionBar = getActionBar();
            actionBar.setTitle(R.string.posts);
            
            if(UIUtils.isIceCreamOrHigher())
                actionBar.setHomeButtonEnabled(true);
        }
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
        Intent intent = new Intent(this, PostsShowCommentsActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }

    private class ShareAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_share;
        }

        public void performAction(View view){
            createShareIntent();
        }

    }
    
    private void createShareIntent(){
        final Intent intent = new Intent(Intent.ACTION_SEND);
        final Formatter formatter = new Formatter();
        String shareText = formatter.format("%s - %s #HabraHabr #HabReader", postsShowFragment.getTitle(), link).toString();

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_link_post));
        intent.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }
    
}