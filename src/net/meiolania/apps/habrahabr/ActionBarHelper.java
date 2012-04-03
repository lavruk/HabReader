package net.meiolania.apps.habrahabr;

import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;

public class ActionBarHelper{
    public final static int ACTIONBAR_POSTS = 0;
    public final static int ACTIONBAR_QA = 1;
    public final static int ACTIONBAR_EVENTS = 2;
    public final static int ACTIONBAR_HUBS = 3;
    public final static int ACTIONBAR_COMPANIES = 4;
    public final static int ACTIONBAR_USERS = 5;
    
    public ActionBar getActionBar(final SherlockActivity activity){
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(actionBar.getThemedContext(), R.array.navigation_titles, android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(spinnerAdapter, new OnNavigationListener(){
            public boolean onNavigationItemSelected(int itemPosition, long itemId){
                Toast.makeText(activity.getApplication(), String.valueOf(itemPosition), Toast.LENGTH_SHORT).show();
                switch((int)itemId){
                    case ACTIONBAR_POSTS:
                        
                        break;
                    case ACTIONBAR_QA:
                        
                        break;
                    case ACTIONBAR_EVENTS:
                        
                        break;
                    case ACTIONBAR_HUBS:
                        
                        break;
                    case ACTIONBAR_COMPANIES:
                        
                        break;
                    case ACTIONBAR_USERS:
                        
                        break;
                }
                return false;
            }
        });
        return actionBar;
    }
    
}