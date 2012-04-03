package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.fragments.CompaniesFragment;
import net.meiolania.apps.habrahabr.activities.fragments.EventsFragment;
import net.meiolania.apps.habrahabr.activities.fragments.HubsFragment;
import net.meiolania.apps.habrahabr.activities.fragments.PostsFragment;
import net.meiolania.apps.habrahabr.activities.fragments.QaFragment;
import net.meiolania.apps.habrahabr.activities.fragments.UsersFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

public class MainActivity extends SherlockFragmentActivity{
    public final static int ACTIONBAR_POSTS = 0;
    public final static int ACTIONBAR_QA = 1;
    public final static int ACTIONBAR_EVENTS = 2;
    public final static int ACTIONBAR_HUBS = 3;
    public final static int ACTIONBAR_COMPANIES = 4;
    public final static int ACTIONBAR_USERS = 5;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main_activity);
        showActionBar();
    }
    
    protected void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(actionBar.getThemedContext(), R.array.navigation_titles, android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(spinnerAdapter, new OnNavigationListener(){
            public boolean onNavigationItemSelected(int itemPosition, long itemId){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch((int)itemId){
                    case ACTIONBAR_POSTS:
                        PostsFragment postsFragment = new PostsFragment();
                        transaction.add(R.id.selected_section_fragment, postsFragment);
                        break;
                    case ACTIONBAR_QA:
                        QaFragment qaFragment = new QaFragment();
                        transaction.add(R.id.selected_section_fragment, qaFragment);
                        break;
                    case ACTIONBAR_EVENTS:
                        EventsFragment eventsFragment = new EventsFragment();
                        transaction.add(R.id.selected_section_fragment, eventsFragment);
                        break;
                    case ACTIONBAR_HUBS:
                        HubsFragment hubsFragment = new HubsFragment();
                        transaction.add(R.id.selected_section_fragment, hubsFragment);
                        break;
                    case ACTIONBAR_COMPANIES:
                        CompaniesFragment companiesFragment = new CompaniesFragment();
                        transaction.add(R.id.selected_section_fragment, companiesFragment);
                        break;
                    case ACTIONBAR_USERS:
                        UsersFragment usersFragment = new UsersFragment();
                        transaction.add(R.id.selected_section_fragment, usersFragment);
                        break;
                }
                transaction.commit();
                return false;
            }
        });
    }
    
}