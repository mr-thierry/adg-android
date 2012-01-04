package com.analysedesgeeks.android;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.MediaController;
import android.widget.TextView;

import com.analysedesgeeks.android.rss.Message;
import com.analysedesgeeks.android.service.RssService;
import com.google.inject.Inject;

public class PodcastActivity extends RoboActivity implements MediaController.MediaPlayerControl {

	@Inject
	private RssService rssService;

	@InjectExtra(Const.EXTRA_POSITION)
	private int position;

	@InjectView(R.id.date)
	private TextView date;

	@InjectView(R.id.description)
	private TextView description;

	private MediaPlayer mediaPlayer;

	private final Handler handler = new Handler();

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		return mediaPlayer.getCurrentPosition();
	}

	@Override
	public int getDuration() {
		return mediaPlayer.getDuration();
	}

	@Override
	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_podcast);

		final Message msg = rssService.getLastFeed().get(position);

		date.setText(msg.formattedDate);
		description.setText(Html.fromHtml(msg.description));

		mediaPlayer = new MediaPlayer();

		try {
			mediaPlayer.setDataSource(msg.link);
			mediaPlayer.prepare();
			mediaPlayer.start();

		} catch (final Exception e) {
			Ln.e(e);
		}

	}

	@Override
	public void pause() {
		mediaPlayer.pause();
	}

	@Override
	public void seekTo(final int i) {
		mediaPlayer.seekTo(i);
	}

	@Override
	public void start() {
		mediaPlayer.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mediaPlayer.stop();
		mediaPlayer.release();
	}
}