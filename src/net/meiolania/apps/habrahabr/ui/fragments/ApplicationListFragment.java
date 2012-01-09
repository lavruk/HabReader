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

package net.meiolania.apps.habrahabr.ui.fragments;

import net.meiolania.apps.habrahabr.Api;
import net.meiolania.apps.habrahabr.Preferences;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public abstract class ApplicationListFragment extends ListFragment{
    protected Preferences preferences;
    protected SharedPreferences sharedPreferences;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        
        preferences = new Preferences(getActivity());
        sharedPreferences = preferences.getSharedPreferences();
    }
    
    protected Api getApi(){
        Api api = new Api(getActivity());
        return api;
    }
    
}