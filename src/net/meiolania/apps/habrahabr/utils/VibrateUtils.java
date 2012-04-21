package net.meiolania.apps.habrahabr.utils;

import android.content.Context;
import android.os.Vibrator;

public class VibrateUtils{
    public final static long DEFAULT_VIBRATE = 50;

    public static void doVibrate(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(DEFAULT_VIBRATE);
    }

    public static void doVibrate(Context context, long milliseconds){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }

    public static void doVibrate(Context context, long[] milliseconds, int repeat){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds, repeat);
    }

}