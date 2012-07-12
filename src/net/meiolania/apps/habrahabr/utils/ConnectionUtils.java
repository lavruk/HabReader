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