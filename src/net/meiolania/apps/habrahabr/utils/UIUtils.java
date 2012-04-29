package net.meiolania.apps.habrahabr.utils;

import android.os.Build;

public class UIUtils{
    
    public static boolean isHoneycomb(){
        return (isHoneycombOrHigher() && !isIceCreamOrHigher());
    }

    public static boolean isHoneycombOrHigher(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean isIceCreamOrHigher(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

}