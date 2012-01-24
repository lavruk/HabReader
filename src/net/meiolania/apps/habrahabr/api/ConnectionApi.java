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

package net.meiolania.apps.habrahabr.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionApi{
    
    public static boolean isConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        
        return (networkInfo != null) ? networkInfo.isConnected() : false;
    }
    
    public static boolean isWiFi(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        
        if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            return networkInfo.isConnected();
        else
            return false;
    }
    
    public static boolean isMobileNetwork(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        
        if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            return networkInfo.isConnected();
        else
            return false;
    }
    
    public static boolean isMobileNetwork(ConnectivityManager connectivityManager, NetworkInfo networkInfo){
        if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            return networkInfo.isConnected();
        else
            return false;
    }
    
    public static boolean isRoaming(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        
        if(isMobileNetwork(connectivityManager, networkInfo) && networkInfo.isRoaming())
            return true;
        else
            return false;
    }
    
}