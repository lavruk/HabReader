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

package net.meiolania.apps.habrahabr.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

public class UIUtils{

    public static boolean isHoneycomb(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean isHoneycombOrHigher(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean isIceCreamOrHigher(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /*
     * http://code.google.com/p/iosched/source/browse/android/src/com/google/android/apps/iosched/util/UIUtils.java#186
     */
    public static boolean isTablet(Context context){
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    
    public static int parseCommentsScore(String score){
        int commentRating = score.charAt(0) == '–' ? -1 : +1;
        try{
            commentRating *= Integer.valueOf(score.substring(1));
        }
        catch(NumberFormatException e){
            commentRating = 0;
        }
        return commentRating;
    }

}