package com.analysedesgeeks.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

public class ActivityController {

	public static void onContactEmailClicked(final Context context) {
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		final Resources res = context.getResources();

		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { Const.ADG_CONTACT_EMAIL });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, res.getString(R.string.contactEmailSubject));

		context.startActivity(Intent.createChooser(emailIntent, res.getString(R.string.sendEmail)));

	}

	public static void onContactMartketClicked(final Context context) {
		final String packageName = context.getApplicationInfo().packageName;
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
	}

	public static void onContactSupportClicked(final Context context) {
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		final Resources res = context.getResources();

		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { Const.ADG_CONTACT_DEV_EMAIL });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, res.getString(R.string.contactEmailSubject));

		context.startActivity(Intent.createChooser(emailIntent, res.getString(R.string.sendEmail)));
	}

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
