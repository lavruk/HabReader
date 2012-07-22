package net.meiolania.apps.habrahabr.utils;

import net.meiolania.apps.habrahabr.R;
import android.content.Context;
import android.content.Intent;

public class IntentUtils{
	public final static String SHARE_TEXT = "%s - %s #Habrahabr #HabReader";

	public static void createShareIntent(Context context, String title, String url){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, String.format(SHARE_TEXT, title, url));
		
		context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
	}

}