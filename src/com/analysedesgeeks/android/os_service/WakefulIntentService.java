package com.analysedesgeeks.android.os_service;

import roboguice.service.RoboIntentService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * Adapted from CommonsWare WakefulIntentService but with Roboguice
 * RoboIntentService as the base class instead
 * 
 * @author troy
 * 
 */
abstract public class WakefulIntentService extends RoboIntentService {
	static final String NAME = "com.analysedesgeeks.android.os_service.WakefulIntentService";

	static final String LAST_ALARM = "lastAlarm";
	private static volatile PowerManager.WakeLock lockStatic = null;

	public static void sendWakefulWork(final Context ctxt, final Class<?> clsService) {
		sendWakefulWork(ctxt, new Intent(ctxt, clsService));
	}

	public static void sendWakefulWork(final Context ctxt, final Intent i) {
		getLock(ctxt.getApplicationContext()).acquire();
		ctxt.startService(i);
	}

	synchronized private static PowerManager.WakeLock getLock(final Context context) {
		if (lockStatic == null) {
			final PowerManager mgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

			lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
			        NAME);
			lockStatic.setReferenceCounted(true);
		}

		return (lockStatic);
	}

	public WakefulIntentService(final String name) {
		super(name);
		setIntentRedelivery(true);
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		if ((flags & START_FLAG_REDELIVERY) != 0) { // if crash restart...
			getLock(this.getApplicationContext()).acquire(); // ...then quick grab the lock
		}

		super.onStartCommand(intent, flags, startId);

		return (START_REDELIVER_INTENT);
	}

	abstract protected void doWakefulWork(Intent intent);

	@Override
	final protected void onHandleIntent(final Intent intent) {
		try {
			doWakefulWork(intent);
		} finally {
			getLock(this.getApplicationContext()).release();
		}
	}

	public interface AlarmListener {
		long getMaxAge();

		void scheduleAlarms(AlarmManager mgr, PendingIntent pi,
		        Context ctxt);

		void sendWakefulWork(Context ctxt);
	}
}