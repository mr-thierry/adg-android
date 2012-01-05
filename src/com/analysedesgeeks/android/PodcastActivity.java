package com.analysedesgeeks.android;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.analysedesgeeks.android.os_service.MediaPlayerService;
import com.analysedesgeeks.android.os_service.WakefulIntentService;
import com.analysedesgeeks.android.rss.Message;
import com.analysedesgeeks.android.service.RssService;
import com.google.inject.Inject;

public class PodcastActivity extends RoboActivity {

	@Inject
	private RssService rssService;

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

		final Message msg = rssService.getLastFeed().get(position);

		date.setText(msg.formattedDate);
		description.setText(Html.fromHtml(msg.description));

		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				playCurrentPodcast();

			}
		});
	}

	private void playCurrentPodcast() {
		final Intent intent = new Intent(this, MediaPlayerService.class);

		final Message msg = rssService.getLastFeed().get(position);

		intent.putExtra(Const.EXTRA_URL, msg.link);
		WakefulIntentService.sendWakefulWork(this, intent);
	}

}