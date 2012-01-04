package com.analysedesgeeks.android;

import android.content.Context;
import android.content.Intent;

public class ActivityController {

	public static void showMainActivity(final Context context) {
		final Intent intent = new Intent(context, MainActivity.class);

		context.startActivity(intent);
	}

	public static void showPodcast(final Context context, final int position) {
		final Intent intent = new Intent(context, PodcastActivity.class);

		intent.putExtra(Const.EXTRA_POSITION, position);

		context.startActivity(intent);

	}

}
