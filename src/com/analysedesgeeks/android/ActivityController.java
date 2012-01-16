package com.analysedesgeeks.android;

import android.content.Context;
import android.content.Intent;

public class ActivityController {

	public static void showSupportPAge(final Context context) {
		final Intent intent = new Intent(context, WebActivity.class);

		intent.putExtra(Const.EXTRA_URL, Const.SUPPORT_URL);

		context.startActivity(intent);

	}

	public static void showInfoActivity(final Context context) {
		final Intent intent = new Intent(context, InfoActivity.class);

		context.startActivity(intent);

	}

	public static void showMainActivity(final Context context) {
		final Intent intent = new Intent(context, MainActivity.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		context.startActivity(intent);
	}

	public static void showPodcast(final Context context, final int position) {
		final Intent intent = new Intent(context, PodcastActivity.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Const.EXTRA_POSITION, position);

		context.startActivity(intent);
	}

}
