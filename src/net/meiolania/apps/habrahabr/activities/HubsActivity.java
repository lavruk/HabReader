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
import net.meiolania.apps.habrahabr.fragments.hubs.HubsFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.MenuItem;

public class HubsActivity extends AbstractionActivity implements OnNavigationListener{
    public final static int LIST_ALL_HUBS = 0;
    public final static int LIST_API = 1;
    public final static int LIST_ADMIN = 2;
    public final static int LIST_DB = 3;
    public final static int LIST_SECURITY = 4;
    public final static int LIST_DESIGN = 5;
    public final static int LIST_GADGETS = 6;
    public final static int LIST_COMPANIES = 7;
    public final static int LIST_MANAGEMENT = 8;
    public final static int LIST_PROGRAMMING = 9;
    public final static int LIST_SOFTWARE = 10;
    public final static int LIST_TELECOMMUNICATIONS = 11;
    public final static int LIST_FRAMEWORKS = 12;
    public final static int LIST_FRONTEND = 13;
    public final static int LIST_OTHERS = 14;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        showActionBar();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(getSupportActionBar().getThemedContext(), R.array.hubs_list, R.layout.sherlock_spinner_item);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(list, this);
    }

    public boolean onNavigationItemSelected(int itemPosition, long itemId){
        HubsFragment hubsFragment = new HubsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch((int)itemId){
            default:
            case LIST_ALL_HUBS:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/page%page%/");
                break;
            case LIST_API:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/api/page%page%/");
                break;
            case LIST_ADMIN:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/administration/page%page%/");
                break;
            case LIST_DB:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/databases/page%page%/");
                break;
            case LIST_SECURITY:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/security/page%page%/");
                break;
            case LIST_DESIGN:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/design-and-media/page%page%/");
                break;
            case LIST_GADGETS:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/hardware/page%page%/");
                break;
            case LIST_COMPANIES:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/companies-and-services/page%page%/");
                break;
            case LIST_MANAGEMENT:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/management/page%page%/");
                break;
            case LIST_PROGRAMMING:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/programming/page%page%/");
                break;
            case LIST_SOFTWARE:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/software/page%page%/");
                break;
            case LIST_TELECOMMUNICATIONS:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/telecommunications/page%page%/");
                break;
            case LIST_FRAMEWORKS:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/fw-and-cms/page%page%/");
                break;
            case LIST_FRONTEND:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/frontend/page%page%/");
                break;
            case LIST_OTHERS:
                hubsFragment.setUrl("http://habrahabr.ru/hubs/others/page%page%/");
                break;
        }
        fragmentTransaction.replace(android.R.id.content, hubsFragment);
        fragmentTransaction.commit();
        return false;
    }
    
}