package net.meiolania.apps.habrahabr.utils;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import android.content.Context;
import android.content.Intent;

public class IntentUtils {

    public static void createShareIntent(Context context, String title, String url) {
	Preferences preferences = Preferences.getInstance(context);
	String shareText = preferences.getShareText();
	
	Intent intent = new Intent(Intent.ACTION_SEND);
	intent.setType("text/plain");
	intent.putExtra(Intent.EXTRA_TEXT, shareText.replace("$link$", url).replace("$title$", title));

	context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }

}