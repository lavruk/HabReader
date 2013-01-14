package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.R;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class AuthActivity extends AbstractionActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        setContentView(R.layout.auth_activity);
    }
    
    @Override
    protected OnClickListener getConnectionDialogListener() {
	return new OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		finish();
	    }
	};
    }

}