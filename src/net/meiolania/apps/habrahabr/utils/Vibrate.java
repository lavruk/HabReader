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
import android.os.Vibrator;

public class Vibrate {
	public final static long DEFAULT_LONG_VIBRATE = 50;
    
	public static void doVibrate(Context context){
		Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(DEFAULT_LONG_VIBRATE);
	}
	
	public static void doVibrate(Context context, long milliseconds){
	    Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
	}
	
	public static void doVibrate(Context context, long[] milliseconds, int repeat){
	    Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds, repeat);
	}

}
