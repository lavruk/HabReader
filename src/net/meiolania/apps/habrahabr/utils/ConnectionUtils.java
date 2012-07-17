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

package net.meiolania.apps.habrahabr.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionUtils{
	
	public static boolean isConnected(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if(manager.getActiveNetworkInfo() != null){
			if(manager.getActiveNetworkInfo().isAvailable() && manager.getActiveNetworkInfo().isConnected())
				return true;
			else
				return false;
		}else
			return false;
	}
	
}