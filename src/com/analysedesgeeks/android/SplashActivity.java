package com.analysedesgeeks.android;

import roboguice.activity.RoboActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.analysedesgeeks.android.service.DownloadService;
import com.analysedesgeeks.android.service.RssService;
import com.google.inject.Inject;

public class SplashActivity extends RoboActivity {

	private static final long MIN_SPLASH_TIME = 3000L;

	@Inject
	private DownloadService downloadService;

	private long startTime;

	private final Handler handler = new Handler();

	@Inject
	private RssService rssService;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		startTime = System.currentTimeMillis();

		new InitializeTask().execute();
	}

	private void done() {
		final long now = System.currentTimeMillis();
		final long diff = now - startTime;
		if (diff < MIN_SPLASH_TIME) {
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					ActivityController.showMainActivity(SplashActivity.this);
					finish();
				}

			}, MIN_SPLASH_TIME - diff);
		} else {
			ActivityController.showMainActivity(this);
			finish();
		}

	}

	private class InitializeTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(final Void... arg0) {
			final boolean downloadSuccess = downloadService.downloadFeed();

			if (!downloadSuccess) {
				rssService.getLastFeed();//S'assure que le dernier download est dans la cache
			}

			return null;
		}

		@Override
		protected void onPostExecute(final Void result) {
			super.onPostExecute(result);
			done();
		}

	}
}