package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.R;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class DashboardActivity extends SherlockActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        showActionBar();
    }

    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.dashboard_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.preferences:
                startActivity(new Intent(this, PreferencesActivity.class));
                break;
            case R.id.more_applications:
                try{
                    Uri uri = Uri.parse("https://market.android.com/developer?pub=Meiolania");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                catch(ActivityNotFoundException e){
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.posts:
                startActivity(new Intent(this, PostsActivity.class));
                break;
            case R.id.hubs:
                startActivity(new Intent(this, HubsActivity.class));
                break;
            case R.id.qa:
                startActivity(new Intent(this, QaActivity.class));
                break;
            case R.id.events:
                startActivity(new Intent(this, EventsActivity.class));
                break;
            case R.id.companies:
                startActivity(new Intent(this, CompaniesActivity.class));
                break;
            case R.id.people:
                startActivity(new Intent(this, PeopleActivity.class));
                break;
        }
    }

}