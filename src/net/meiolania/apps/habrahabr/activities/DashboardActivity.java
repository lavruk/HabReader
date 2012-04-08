package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.R;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

public class DashboardActivity extends SherlockActivity{
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
    }
    
}