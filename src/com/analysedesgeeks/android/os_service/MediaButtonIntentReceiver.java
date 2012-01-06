package com.analysedesgeeks.android.os_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

/**
 * 
 */
public class MediaButtonIntentReceiver extends BroadcastReceiver {

	private static final int MSG_LONGPRESS_TIMEOUT = 1;
	private static final int LONG_PRESS_DELAY = 1000;

	private static long mLastClickTime = 0;
	private static boolean mDown = false;
	private static boolean mLaunched = false;

	private static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {
			case MSG_LONGPRESS_TIMEOUT:
				if (!mLaunched) {
					final Context context = (Context) msg.obj;
					final Intent i = new Intent();
					i.putExtra("autoshuffle", "true");
					i.setClass(context, PodcastPlaybackService.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
					mLaunched = true;
				}
				break;
			}
		}
	};

	@Override
	public void onReceive(final Context context, final Intent intent) {
		final String intentAction = intent.getAction();
		if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intentAction)) {
			final Intent i = new Intent(context, PodcastPlaybackService.class);
			i.setAction(PodcastPlaybackService.SERVICECMD);
			i.putExtra(PodcastPlaybackService.CMDNAME, PodcastPlaybackService.CMDPAUSE);
			context.startService(i);
		} else if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
			final KeyEvent event = (KeyEvent)
			        intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

			if (event == null) {
				return;
			}

			final int keycode = event.getKeyCode();
			final int action = event.getAction();
			final long eventtime = event.getEventTime();

			// single quick press: pause/resume. 
			// double press: next track
			// long press: start auto-shuffle mode.

			String command = null;
			switch (keycode) {
			case KeyEvent.KEYCODE_MEDIA_STOP:
				command = PodcastPlaybackService.CMDSTOP;
				break;
			case KeyEvent.KEYCODE_HEADSETHOOK:
			case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
				command = PodcastPlaybackService.CMDTOGGLEPAUSE;
				break;

			}

			if (command != null) {
				if (action == KeyEvent.ACTION_DOWN) {
					if (mDown) {
						if (PodcastPlaybackService.CMDTOGGLEPAUSE.equals(command)
						        && mLastClickTime != 0
						        && eventtime - mLastClickTime > LONG_PRESS_DELAY) {
							mHandler.sendMessage(
							        mHandler.obtainMessage(MSG_LONGPRESS_TIMEOUT, context));
						}
					} else {
						// if this isn't a repeat event

						// The service may or may not be running, but we need to send it
						// a command.
						final Intent i = new Intent(context, PodcastPlaybackService.class);
						i.setAction(PodcastPlaybackService.SERVICECMD);
						if (keycode == KeyEvent.KEYCODE_HEADSETHOOK &&
						        eventtime - mLastClickTime < 300) {

						} else {
							i.putExtra(PodcastPlaybackService.CMDNAME, command);
							context.startService(i);
							mLastClickTime = eventtime;
						}

						mLaunched = false;
						mDown = true;
					}
				} else {
					mHandler.removeMessages(MSG_LONGPRESS_TIMEOUT);
					mDown = false;
				}
				if (isOrderedBroadcast()) {
					abortBroadcast();
				}
			}
		}
	}
}
