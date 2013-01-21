package net.meiolania.apps.habrahabr.fragments.posts;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.TabListener;
import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;

public class PostsMainFragment extends SherlockFragment {

    public static PostsMainFragment newInstance() {
	return new PostsMainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	showActionBar();
    }
    
    private void showActionBar() {
	SherlockFragmentActivity activity = getSherlockActivity();
	
	ActionBar actionBar = activity.getSupportActionBar();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setTitle(R.string.posts);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	Preferences preferences = Preferences.getInstance(activity);
	int selectedTab = preferences.getPostsDefaultTab();

	/* Best tab */
	Tab tab = actionBar.newTab();
	tab.setText(R.string.best);
	tab.setTag("best");
	tab.setTabListener(new TabListener<PostsBestFragment>(activity, "best", PostsBestFragment.class));
	actionBar.addTab(tab, (selectedTab == 0 ? true : false));

	/* Thematic tab */
	tab = actionBar.newTab();
	tab.setText(R.string.thematic);
	tab.setTag("thematic");
	tab.setTabListener(new TabListener<PostsThematicFragment>(activity, "thematic", PostsThematicFragment.class));
	actionBar.addTab(tab, (selectedTab == 1 ? true : false));

	/* Corporate tab */
	tab = actionBar.newTab();
	tab.setText(R.string.corporate);
	tab.setTag("corporate");
	tab.setTabListener(new TabListener<PostsCorporateFragment>(activity, "corporate", PostsCorporateFragment.class));
	actionBar.addTab(tab, (selectedTab == 2 ? true : false));
    }
}