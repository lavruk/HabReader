package net.meiolania.apps.habrahabr.ui.actions;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.activities.DashboardActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.markupartist.android.widget.ActionBar.Action;

public class HomeAction implements Action{
    private Context context;
    private boolean noPerformAction = false;
    
    public HomeAction(Context context){
        this.context = context;
    }
    
    public HomeAction(Context context, boolean noPerformAction){
        this(context);
        this.noPerformAction = noPerformAction;
    }
    
    public int getDrawable(){
        return R.drawable.actionbar_ic_home;
    }

    public void performAction(View view){
        if(!noPerformAction){
            Intent intent = new Intent(context, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

}