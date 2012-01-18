package com.analysedesgeeks.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ActivityController {

	public static void onUrlClicked(final Context context, final String url) {

		final Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));

		context.startActivity(intent);
	}

	public static void showAboutPage(final Context context) {
		final Intent intent = new Intent(context, AboutActivity.class);

		context.startActivity(intent);
	}

	public static void showContact(final Context context) {
		final Intent intent = new Intent(context, ContactActivity.class);

		context.startActivity(intent);
	}

	public static void showFacebook(final Context context) {
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(Const.FACEBOOK_URL));

		context.startActivity(intent);
	}

	public static void showForum(final Context context) {
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(Const.FORUM_URL));

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

	public static void showSupportPage(final Context context) {
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(Const.SUPPORT_URL));

		context.startActivity(intent);

	}

	public static void showWebPage(final Context context) {
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(Const.WEB_URL));

		context.startActivity(intent);
	}
}
