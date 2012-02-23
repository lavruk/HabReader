/*
   Copyright (C) 2011 Andrey Zaytsev <a.einsam@gmail.com>
  
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

package net.meiolania.apps.habrahabr.ui.actions;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.ui.dashboard.DashboardActivity;
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