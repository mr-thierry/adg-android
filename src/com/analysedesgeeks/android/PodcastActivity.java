package com.analysedesgeeks.android;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.analysedesgeeks.android.MusicUtils.ServiceToken;
import com.analysedesgeeks.android.os_service.IMediaPlaybackService;
import com.analysedesgeeks.android.os_service.PodcastPlaybackService;
import com.analysedesgeeks.android.rss.FeedItem;
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

	private boolean paused;

	private IMediaPlaybackService mService = null;

	private static final int REFRESH = 1;
	private static final int QUIT = 2;

	@InjectView(R.id.pause)
	private ImageButton mPauseButton;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {

			case REFRESH:
				final long next = refreshNow();
				queueNextRefresh(next);
				break;

			default:
				break;
			}
		}
	};

	private long mDuration;

	private final long mPosOverride = -1;

	@InjectView(R.id.currenttime)
	private TextView mCurrentTime;

	@InjectView(android.R.id.progress)
	private ProgressBar mProgress;

	@InjectView(R.id.totaltime)
	private TextView mTotalTime;

	private final ServiceConnection osc = new ServiceConnection() {
		@Override
		public void onServiceConnected(final ComponentName classname, final IBinder obj) {
			mService = IMediaPlaybackService.Stub.asInterface(obj);

			try {
				// Assume something is playing when the service says it is,
				// but also if the audio ID is valid but the service is paused.
				if (mService.isPlaying()) {
					setPauseButtonImage();
					return;
				}
			} catch (final RemoteException ex) {
			}

		}

		@Override
		public void onServiceDisconnected(final ComponentName classname) {
			mService = null;
		}
	};

	private ServiceToken mToken;

	private final Handler handler = new Handler();

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
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						playCurrentPodcast();
					}

				}, 500);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		updateTrackInfo();
		setPauseButtonImage();
	}

	@Override
	public void onStart() {
		super.onStart();
		paused = false;

		mToken = MusicUtils.bindToService(this, osc);
		if (mToken == null) {
			// something went wrong
			mHandler.sendEmptyMessage(QUIT);
		}

		final IntentFilter f = new IntentFilter();
		f.addAction(PodcastPlaybackService.PLAYSTATE_CHANGED);
		updateTrackInfo();
		final long next = refreshNow();
		queueNextRefresh(next);
	}

	@Override
	public void onStop() {
		paused = true;
		mHandler.removeMessages(REFRESH);
		MusicUtils.unbindFromService(mToken);
		mService = null;
		super.onStop();
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
			setIntent(new Intent());
		} catch (final Exception ex) {
			Log.d("MediaPlaybackActivity", "couldn't start playback: " + ex);
		}

		updateTrackInfo();
		final long next = refreshNow();
		queueNextRefresh(next);

	}

	private void queueNextRefresh(final long delay) {
		if (!paused) {
			final Message msg = mHandler.obtainMessage(REFRESH);
			mHandler.removeMessages(REFRESH);
			mHandler.sendMessageDelayed(msg, delay);
		}
	}

	private long refreshNow() {
		if (mService == null) {
			return 500;
		}
		try {

			final long pos = mService.position();
			long remaining = 1000 - (pos % 1000);
			if (mDuration > 0) {
				mCurrentTime.setText(MusicUtils.makeTimeString(this, pos / 1000));

				if (mService.isPlaying()) {
					mCurrentTime.setVisibility(View.VISIBLE);
				} else {
					// blink the counter
					final int vis = mCurrentTime.getVisibility();
					mCurrentTime.setVisibility(vis == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
					remaining = 500;
				}

				mProgress.setProgress((int) (1000 * pos / mDuration));
			} else {
				mCurrentTime.setText("--:--");
				mProgress.setProgress(1000);
			}
			// return the number of milliseconds until the next full second, so
			// the counter can be updated at just the right time
			return remaining;
		} catch (final RemoteException ex) {
		}
		return 500;
	}

	private void setPauseButtonImage() {
		try {
			if (mService != null && mService.isPlaying()) {
				mPauseButton.setImageResource(android.R.drawable.ic_media_pause);
			} else {
				mPauseButton.setImageResource(android.R.drawable.ic_media_play);
			}
		} catch (final RemoteException ex) {
		}
	}

	private void updateTrackInfo() {
		if (mService == null) {
			return;
		}
		try {
			final String path = mService.getPath();
			if (path == null) {
				finish();
				return;
			}

			mDuration = mService.duration();
			mTotalTime.setText(MusicUtils.makeTimeString(this, mDuration / 1000));
		} catch (final RemoteException ex) {
			finish();
		}
	}

}