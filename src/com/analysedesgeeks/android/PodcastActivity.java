package com.analysedesgeeks.android;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.analysedesgeeks.android.rss.FeedItem;

public class PodcastActivity extends BaseAbstractActivity {

	@InjectExtra(Const.EXTRA_POSITION)
	private int position;

	@InjectView(R.id.date)
	private TextView date;

	@InjectView(R.id.description)
	private TextView description;

	@InjectView(R.id.play)
	private ImageButton playButton;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_podcast);

		final FeedItem msg = rssService.getLastFeed().get(position);

		date.setText(msg.formattedDate);
		description.setText(Html.fromHtml(msg.description));

		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				podcastPlayer.setVisibility(View.VISIBLE);
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						playCurrentPodcast();
					}

				}, 500);
			}
		});
	}

	private void playCurrentPodcast() {

		final FeedItem msg = rssService.getLastFeed().get(position);

		if (mService == null) {
			return;
		}

		try {
			mService.stop();
			mService.openFile(msg.link);
			mService.play();
			mService.setDescription(msg.title);

			setIntent(new Intent());
		} catch (final Exception ex) {
			Log.d("MediaPlaybackActivity", "couldn't start playback: " + ex);
		}

		updatePodcastInfo();

	}

}