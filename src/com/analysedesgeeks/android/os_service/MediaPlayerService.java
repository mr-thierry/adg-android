package com.analysedesgeeks.android.os_service;

import roboguice.util.Ln;
import android.content.Intent;
import android.media.MediaPlayer;

import com.analysedesgeeks.android.Const;
import com.analysedesgeeks.android.utils.Utils;

public class MediaPlayerService extends WakefulIntentService {

	private MediaPlayer mediaPlayer;

	private final String currentSource = null;

	public MediaPlayerService() {
		super("MediaPlayerService");
	}

	@Override
	protected void doWakefulWork(final Intent intent) {
		try {
			final String url = intent.getStringExtra(Const.EXTRA_URL);

			if (mediaPlayer == null) {
				mediaPlayer = new MediaPlayer();
			}

			if (Utils.isNotEmpty(url) && !url.equals(currentSource)) {
				mediaPlayer.setDataSource(url);
				mediaPlayer.prepare();
				mediaPlayer.start();
			}

		} catch (final Exception e) {
			Ln.e(e);
		}
	}

}
