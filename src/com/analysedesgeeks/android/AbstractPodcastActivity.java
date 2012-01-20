/*
 * Copyright (C) 2012 Thierry-Dimitri Roy <thierryd@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.analysedesgeeks.android;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.analysedesgeeks.android.MusicUtils.ServiceToken;
import com.analysedesgeeks.android.os_service.IMediaPlaybackService;
import com.analysedesgeeks.android.os_service.PodcastPlaybackService;
import com.analysedesgeeks.android.service.RssService;
import com.analysedesgeeks.android.utils.Utils;
import com.google.inject.Inject;

public abstract class AbstractPodcastActivity extends RoboFragmentActivity {

	private static final int MENU_INFO = 1;

	@Inject
	protected RssService rssService;

	private boolean paused;

	protected IMediaPlaybackService mService = null;

	private static final int REFRESH = 1;

	private static final int QUIT = 2;

	protected static final int LOADING_INDEX = 0;

	protected static final int CONTENT_INDEX = 1;

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

	@InjectView(R.id.currenttime)
	private TextView mCurrentTime;

	@InjectView(R.id.playing)
	private TextView mPlaying;

	@InjectView(android.R.id.progress)
	private SeekBar mProgress;

	@InjectView(R.id.totaltime)
	private TextView mTotalTime;

	@InjectView(R.id.podcast_player)
	protected ViewSwitcher podcastPlayer;

	private final ServiceConnection osc = new ServiceConnection() {
		@Override
		public void onServiceConnected(final ComponentName classname, final IBinder obj) {
			mService = IMediaPlaybackService.Stub.asInterface(obj);

			try {
				// Assume something is playing when the service says it is,
				// but also if the audio ID is valid but the service is paused.
				if (Utils.isNotEmpty(mService.getPath())) {
					podcastPlayer.setVisibility(View.VISIBLE);
					updateTrackInfo();
					refreshNow();
					return;
				} else {
					podcastPlayer.setVisibility(View.GONE);
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

	protected final Handler handler = new Handler();

	private final View.OnClickListener mPauseListener = new View.OnClickListener() {
		@Override
		public void onClick(final View v) {
			doPauseResume();
		}
	};

	private long mLastSeekEventTime;

	private long mPosOverride = -1;
	private boolean mFromTouch = false;
	private final OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(final SeekBar bar, final int progress, final boolean fromuser) {
			if (!fromuser || (mService == null)) {
				return;
			}
			final long now = SystemClock.elapsedRealtime();
			if ((now - mLastSeekEventTime) > 250) {
				mLastSeekEventTime = now;
				mPosOverride = mDuration * progress / 1000;
				try {
					mService.seek(mPosOverride);
				} catch (final RemoteException ex) {
				}

				// trackball event, allow progress updates
				if (!mFromTouch) {
					refreshNow();
					mPosOverride = -1;
				}
			}
		}

		@Override
		public void onStartTrackingTouch(final SeekBar bar) {
			mLastSeekEventTime = 0;
			mFromTouch = true;
		}

		@Override
		public void onStopTrackingTouch(final SeekBar bar) {
			mPosOverride = -1;
			mFromTouch = false;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		menu.add(0, MENU_INFO, 0, "Info")
		        .setIcon(R.drawable.ic_menu_info)
		        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case MENU_INFO:
			ActivityController.showInfoActivity(this);
			return true;
		case android.R.id.home:
			// app icon in action bar clicked; go home
			ActivityController.showMainActivity(this);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		mPauseButton.setOnClickListener(mPauseListener);

		mProgress.setOnSeekBarChangeListener(mSeekListener);

		mProgress.setMax(1000);

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

	protected void queueNextRefresh(final long delay) {
		if (!paused) {
			final Message msg = mHandler.obtainMessage(REFRESH);
			mHandler.removeMessages(REFRESH);
			mHandler.sendMessageDelayed(msg, delay);
		}
	}

	protected long refreshNow() {
		if (mService == null) {
			return 500;
		}
		try {

			if (mService.hasPlayed()) {
				podcastPlayer.setDisplayedChild(CONTENT_INDEX);
			}

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
			mPlaying.setText(mService.getDescription());
			setPauseButtonImage();

			// return the number of milliseconds until the next full second, so
			// the counter can be updated at just the right time
			return remaining;
		} catch (final RemoteException ex) {
		}
		return 500;
	}

	protected void updatePodcastInfo() {
		if (mService == null) {
			return;
		}

		try {
			if (mService.isPlaying()) {
				updateTrackInfo();
				final long next = refreshNow();
				queueNextRefresh(next);

				podcastPlayer.setVisibility(View.VISIBLE);
			}
		} catch (final RemoteException e) {
			Ln.e(e);
		}

	}

	protected void updateTrackInfo() {
		if (mService == null) {
			return;
		}
		try {
			mDuration = mService.duration();
			mTotalTime.setText(MusicUtils.makeTimeString(this, mDuration / 1000));

			if (mService.hasPlayed()) {
				podcastPlayer.setDisplayedChild(CONTENT_INDEX);
			}
		} catch (final RemoteException ex) {
			finish();
		}
	}

	private void doPauseResume() {
		try {
			if (mService != null) {
				if (mService.isPlaying()) {
					mService.pause();
				} else {
					mService.play();
				}
				refreshNow();
			}
		} catch (final RemoteException ex) {
		}
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

}