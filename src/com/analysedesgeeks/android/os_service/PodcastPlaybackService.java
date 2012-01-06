package com.analysedesgeeks.android.os_service;

import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.util.Log;
import android.widget.RemoteViews;

import com.analysedesgeeks.android.R;

public class PodcastPlaybackService extends Service {

	private boolean mIsSupposedToBePlaying = false;

	private int mServiceStartId = -1;

	public static final int PLAYBACKSERVICE_STATUS = 1;

	/**
	 * interval after which we stop the service when idle
	 */
	private static final int IDLE_DELAY = 60000;

	/**
	 * used to track what type of audio focus loss caused the playback to pause
	 */
	private boolean mPausedByTransientLossOfFocus = false;

	public static final String CMDTOGGLEPAUSE = "togglepause";
	public static final String CMDNAME = "command";
	public static final String CMDPAUSE = "pause";
	public static final String CMDSTOP = "stop";

	public static final String TOGGLEPAUSE_ACTION = "com.android.music.musicservicecommand.togglepause";
	public static final String PAUSE_ACTION = "com.android.music.musicservicecommand.pause";
	public static final String PLAYSTATE_CHANGED = "com.android.music.playstatechanged";
	public static final String SERVICECMD = "com.android.music.musicservicecommand";

	private MultiPlayer mPlayer;

	private final IBinder mBinder = new ServiceStub(this);

	private boolean mServiceInUse = false;

	private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			final String cmd = intent.getStringExtra("command");

			if (CMDTOGGLEPAUSE.equals(cmd) || TOGGLEPAUSE_ACTION.equals(action)) {
				if (isPlaying()) {
					pause();
					mPausedByTransientLossOfFocus = false;
				} else {
					play();
				}
			} else if (CMDPAUSE.equals(cmd) || PAUSE_ACTION.equals(action)) {
				pause();
				mPausedByTransientLossOfFocus = false;
			} else if (CMDSTOP.equals(cmd)) {
				pause();
				mPausedByTransientLossOfFocus = false;
				seek(0);
			}
		}
	};

	private final Handler mDelayedStopHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			// Check again to make sure nothing is playing right now
			if (isPlaying() || mPausedByTransientLossOfFocus || mServiceInUse || mMediaplayerHandler.hasMessages(TRACK_ENDED)) {
				return;
			}
			stopSelf(mServiceStartId);
		}
	};

	private final Handler mMediaplayerHandler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {
			case RELEASE_WAKELOCK:
				mWakeLock.release();
				break;
			default:
				break;
			}
		}
	};

	private WakeLock mWakeLock;

	private static final int TRACK_ENDED = 1;

	/*
	 * Desired behavior for prev/next/shuffle:
	 * 
	 * - NEXT will move to the next track in the list when not shuffling, and to
	 * a track randomly picked from the not-yet-played tracks when shuffling.
	 * If all tracks have already been played, pick from the full set, but
	 * avoid picking the previously played track if possible.
	 * - when shuffling, PREV will go to the previously played track. Hitting
	 * PREV
	 * again will go to the track played before that, etc. When the start of the
	 * history has been reached, PREV is a no-op.
	 * When not shuffling, PREV will go to the sequentially previous track (the
	 * difference with the shuffle-case is mainly that when not shuffling, the
	 * user can back up to tracks that are not in the history).
	 * 
	 * Example:
	 * When playing an album with 10 tracks from the start, and enabling shuffle
	 * while playing track 5, the remaining tracks (6-10) will be shuffled, e.g.
	 * the final play order might be 1-2-3-4-5-8-10-6-9-7.
	 * When hitting 'prev' 8 times while playing track 7 in this example, the
	 * user will go to tracks 9-6-10-8-5-4-3-2. If the user then hits 'next',
	 * a random track will be picked again. If at any time user disables
	 * shuffling
	 * the next/previous track will be picked in sequential order again.
	 */

	private static final int RELEASE_WAKELOCK = 2;

	private static final int SERVER_DIED = 3;

	private AudioManager mAudioManager;

	private String mFileToPlay;

	/**
	 * Returns the duration of the file in milliseconds.
	 * Currently this method returns -1 for the duration of MIDI files.
	 */
	public long duration() {
		if (mPlayer.isInitialized()) {
			return mPlayer.duration();
		}
		return -1;
	}

	/**
	 * Returns the path of the currently playing file, or null if
	 * no file is currently playing.
	 */
	public String getPath() {
		return mFileToPlay;
	}

	/**
	 * Returns whether something is currently playing
	 * 
	 * @return true if something is playing (or will be playing shortly, in case
	 *         we're currently transitioning between tracks), false if not.
	 */
	public boolean isPlaying() {
		return mIsSupposedToBePlaying;
	}

	@Override
	public IBinder onBind(final Intent intent) {
		mDelayedStopHandler.removeCallbacksAndMessages(null);
		mServiceInUse = true;
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.registerMediaButtonEventReceiver(new ComponentName(getPackageName(), MediaButtonIntentReceiver.class.getName()));

		// Needs to be done in this thread, since otherwise ApplicationContext.getPowerManager() crashes.
		mPlayer = new MultiPlayer();
		mPlayer.setHandler(mMediaplayerHandler);

		final IntentFilter commandFilter = new IntentFilter();
		commandFilter.addAction(SERVICECMD);
		commandFilter.addAction(TOGGLEPAUSE_ACTION);
		commandFilter.addAction(PAUSE_ACTION);

		registerReceiver(mIntentReceiver, commandFilter);

		final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
		mWakeLock.setReferenceCounted(false);

		// If the service was idle, but got killed before it stopped itself, the
		// system will relaunch it. Make sure it gets stopped again in that case.
		final Message msg = mDelayedStopHandler.obtainMessage();
		mDelayedStopHandler.sendMessageDelayed(msg, IDLE_DELAY);
	}

	@Override
	public void onDestroy() {

		mPlayer.release();
		mPlayer = null;

		// make sure there aren't any other messages coming
		mDelayedStopHandler.removeCallbacksAndMessages(null);
		mMediaplayerHandler.removeCallbacksAndMessages(null);

		unregisterReceiver(mIntentReceiver);

		mWakeLock.release();
		super.onDestroy();
	}

	@Override
	public void onRebind(final Intent intent) {
		mDelayedStopHandler.removeCallbacksAndMessages(null);
		mServiceInUse = true;
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		mServiceStartId = startId;
		mDelayedStopHandler.removeCallbacksAndMessages(null);

		if (intent != null) {
			final String action = intent.getAction();
			final String cmd = intent.getStringExtra("command");

			if (CMDTOGGLEPAUSE.equals(cmd) || TOGGLEPAUSE_ACTION.equals(action)) {
				if (isPlaying()) {
					pause();
					mPausedByTransientLossOfFocus = false;
				} else {
					play();
				}
			} else if (CMDPAUSE.equals(cmd) || PAUSE_ACTION.equals(action)) {
				pause();
				mPausedByTransientLossOfFocus = false;
			} else if (CMDSTOP.equals(cmd)) {
				pause();
				mPausedByTransientLossOfFocus = false;
				seek(0);
			}
		}

		// make sure the service will shut down on its own if it was
		// just started but not bound to and nothing is playing
		mDelayedStopHandler.removeCallbacksAndMessages(null);
		final Message msg = mDelayedStopHandler.obtainMessage();
		mDelayedStopHandler.sendMessageDelayed(msg, IDLE_DELAY);
		return START_STICKY;
	}

	@Override
	public boolean onUnbind(final Intent intent) {
		mServiceInUse = false;

		if (isPlaying() || mPausedByTransientLossOfFocus) {
			// something is currently playing, or will be playing once 
			// an in-progress action requesting audio focus ends, so don't stop the service now.
			return true;
		}

		// No active playlist, OK to stop the service right now
		stopSelf(mServiceStartId);
		return true;
	}

	/**
	 * Opens the specified file and readies it for playback.
	 * 
	 * @param path
	 *            The full path of the file to be opened.
	 */
	public void open(final String path) {
		synchronized (this) {
			if (path == null) {
				return;
			}

			mFileToPlay = path;
			mPlayer.setDataSource(mFileToPlay);
			if (!mPlayer.isInitialized()) {
				stop(true);
			}
		}
	}

	/**
	 * Pauses playback (call play() to resume)
	 */
	public void pause() {
		synchronized (this) {
			if (isPlaying()) {
				mPlayer.pause();
				gotoIdleState();
				mIsSupposedToBePlaying = false;
			}
		}
	}

	/**
	 * Starts playback of a previously opened file.
	 */
	public void play() {
		mAudioManager.registerMediaButtonEventReceiver(new ComponentName(this.getPackageName(), MediaButtonIntentReceiver.class.getName()));

		if (mPlayer.isInitialized()) {
			mPlayer.start();

			final RemoteViews views = new RemoteViews(getPackageName(), R.layout.statusbar);
			views.setImageViewResource(R.id.icon, R.drawable.stat_notify_musicplayer);

			views.setTextViewText(R.id.trackname, getPath());
			views.setTextViewText(R.id.artistalbum, null);

			final Notification status = new Notification();
			status.contentView = views;
			status.flags |= Notification.FLAG_ONGOING_EVENT;
			status.icon = R.drawable.stat_notify_musicplayer;
			status.contentIntent = PendingIntent.getActivity(this, 0, new Intent("com.android.music.PLAYBACK_VIEWER").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
			startForeground(PLAYBACKSERVICE_STATUS, status);
			if (!mIsSupposedToBePlaying) {
				mIsSupposedToBePlaying = true;
			}

		}
	}

	/**
	 * Returns the current playback position in milliseconds
	 */
	public long position() {
		if (mPlayer.isInitialized()) {
			return mPlayer.position();
		}
		return -1;
	}

	/**
	 * Seeks to the position specified.
	 * 
	 * @param pos
	 *            The position to seek to, in milliseconds
	 */
	public long seek(long pos) {
		if (mPlayer.isInitialized()) {
			if (pos < 0) {
				pos = 0;
			}
			if (pos > mPlayer.duration()) {
				pos = mPlayer.duration();
			}
			return mPlayer.seek(pos);
		}
		return -1;
	}

	/**
	 * Stops playback.
	 */
	public void stop() {
		stop(true);
	}

	private void gotoIdleState() {
		mDelayedStopHandler.removeCallbacksAndMessages(null);
		final Message msg = mDelayedStopHandler.obtainMessage();
		mDelayedStopHandler.sendMessageDelayed(msg, IDLE_DELAY);
		stopForeground(true);
	}

	private void stop(final boolean remove_status_icon) {
		if (mPlayer.isInitialized()) {
			mPlayer.stop();
		}
		mFileToPlay = null;

		if (remove_status_icon) {
			gotoIdleState();
		} else {
			stopForeground(false);
		}
		if (remove_status_icon) {
			mIsSupposedToBePlaying = false;
		}
	}

	/*
	 * By making this a static class with a WeakReference to the Service, we
	 * ensure that the Service can be GCd even when the system process still
	 * has a remote reference to the stub.
	 */
	static class ServiceStub extends IMediaPlaybackService.Stub {
		WeakReference<PodcastPlaybackService> mService;

		ServiceStub(final PodcastPlaybackService service) {
			mService = new WeakReference<PodcastPlaybackService>(service);
		}

		@Override
		public long duration() {
			return mService.get().duration();
		}

		@Override
		public String getPath() throws RemoteException {
			return mService.get().getPath();
		}

		@Override
		public boolean isPlaying() {
			return mService.get().isPlaying();
		}

		@Override
		public void openFile(final String path)
		{
			mService.get().open(path);
		}

		@Override
		public void pause() {
			mService.get().pause();
		}

		@Override
		public void play() {
			mService.get().play();
		}

		@Override
		public long position() throws RemoteException {
			return mService.get().position();
		}

		@Override
		public long seek(final long pos) throws RemoteException {
			return mService.get().seek(pos);
		}

		@Override
		public void stop() {
			mService.get().stop();
		}

	}

	/**
	 * Provides a unified interface for dealing with midi files and
	 * other media files.
	 */
	private class MultiPlayer {
		private MediaPlayer mMediaPlayer = new MediaPlayer();
		private Handler mHandler;
		private boolean mIsInitialized = false;

		MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(final MediaPlayer mp) {
				// Acquire a temporary wakelock, since when we return from
				// this callback the MediaPlayer will release its wakelock
				// and allow the device to go to sleep.
				// This temporary wakelock is released when the RELEASE_WAKELOCK
				// message is processed, but just in case, put a timeout on it.
				mWakeLock.acquire(30000);
				mHandler.sendEmptyMessage(TRACK_ENDED);
				mHandler.sendEmptyMessage(RELEASE_WAKELOCK);
			}
		};

		MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(final MediaPlayer mp, final int what, final int extra) {
				switch (what) {
				case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
					mIsInitialized = false;
					mMediaPlayer.release();
					// Creating a new MediaPlayer and settings its wakemode does not
					// require the media service, so it's OK to do this now, while the
					// service is still being restarted
					mMediaPlayer = new MediaPlayer();
					mMediaPlayer.setWakeMode(PodcastPlaybackService.this, PowerManager.PARTIAL_WAKE_LOCK);
					mHandler.sendMessageDelayed(mHandler.obtainMessage(SERVER_DIED), 2000);
					return true;
				default:
					Log.d("MultiPlayer", "Error: " + what + "," + extra);
					break;
				}
				return false;
			}
		};

		public MultiPlayer() {
			mMediaPlayer.setWakeMode(PodcastPlaybackService.this, PowerManager.PARTIAL_WAKE_LOCK);
		}

		public long duration() {
			return mMediaPlayer.getDuration();
		}

		public boolean isInitialized() {
			return mIsInitialized;
		}

		public void pause() {
			mMediaPlayer.pause();
		}

		public long position() {
			return mMediaPlayer.getCurrentPosition();
		}

		/**
		 * You CANNOT use this player anymore after calling release()
		 */
		public void release() {
			stop();
			mMediaPlayer.release();
		}

		public long seek(final long whereto) {
			mMediaPlayer.seekTo((int) whereto);
			return whereto;
		}

		public void setDataSource(final String path) {
			try {
				mMediaPlayer.reset();
				mMediaPlayer.setOnPreparedListener(null);
				if (path.startsWith("content://")) {
					mMediaPlayer.setDataSource(PodcastPlaybackService.this, Uri.parse(path));
				} else {
					mMediaPlayer.setDataSource(path);
				}
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mMediaPlayer.prepare();
			} catch (final IOException ex) {
				// TODO: notify the user why the file couldn't be opened
				mIsInitialized = false;
				return;
			} catch (final IllegalArgumentException ex) {
				// TODO: notify the user why the file couldn't be opened
				mIsInitialized = false;
				return;
			}
			mMediaPlayer.setOnCompletionListener(listener);
			mMediaPlayer.setOnErrorListener(errorListener);
			mIsInitialized = true;
		}

		public void setHandler(final Handler handler) {
			mHandler = handler;
		}

		public void start() {
			mMediaPlayer.start();
		}

		public void stop() {
			mMediaPlayer.reset();
			mIsInitialized = false;
		}
	}
}
