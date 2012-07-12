/*
Copyright 2012 Andrey Zaytsev

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

package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.R;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class DashboardActivity extends AbstractionActivity implements OnClickListener{

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
        
        final EditText searchQuery = (EditText) menu.findItem(R.id.search).getActionView().findViewById(R.id.search_query);
        searchQuery.setOnEditorActionListener(new OnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    Intent intent = new Intent(DashboardActivity.this, PostsSearchActivity.class);
                    intent.putExtra(PostsSearchActivity.EXTRA_QUERY, searchQuery.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        
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

	@Override
	protected android.content.DialogInterface.OnClickListener getConnectionDialogListener(){
		return new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.dismiss();
			}
		};
	}

}