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

import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

import com.analysedesgeeks.android.os_service.IMediaPlaybackService;
import com.analysedesgeeks.android.os_service.PodcastPlaybackService;

public class MusicUtils {

	private static StringBuilder sFormatBuilder = new StringBuilder();

	private static final Object[] sTimeArgs = new Object[5];

	private static Formatter sFormatter = new Formatter(sFormatBuilder, Locale.getDefault());

	private static HashMap<Context, ServiceBinder> sConnectionMap = new HashMap<Context, ServiceBinder>();

	public static IMediaPlaybackService sService = null;

	public static ServiceToken bindToService(final Activity context) {
		return bindToService(context, null);
	}

	public static ServiceToken bindToService(final Activity context, final ServiceConnection callback) {
		Activity realActivity = context.getParent();
		if (realActivity == null) {
			realActivity = context;
		}
		final ContextWrapper cw = new ContextWrapper(realActivity);
		cw.startService(new Intent(cw, PodcastPlaybackService.class));
		final ServiceBinder sb = new ServiceBinder(callback);
		if (cw.bindService((new Intent()).setClass(cw, PodcastPlaybackService.class), sb, 0)) {
			sConnectionMap.put(cw, sb);
			return new ServiceToken(cw);
		}
		Log.e("Music", "Failed to bind to service");
		return null;
	}

	public static String makeTimeString(final Context context, final long secs) {
		final String durationformat = context.getString(
		        secs < 3600 ? R.string.durationformatshort : R.string.durationformatlong);

		sFormatBuilder.setLength(0);

		final Object[] timeArgs = sTimeArgs;
		timeArgs[0] = secs / 3600;
		timeArgs[1] = secs / 60;
		timeArgs[2] = (secs / 60) % 60;
		timeArgs[3] = secs;
		timeArgs[4] = secs % 60;

		return sFormatter.format(durationformat, timeArgs).toString();
	}

	public static void unbindFromService(final ServiceToken token) {
		if (token == null) {
			Log.e("MusicUtils", "Trying to unbind with null token");
			return;
		}
		final ContextWrapper cw = token.mWrappedContext;
		final ServiceBinder sb = sConnectionMap.remove(cw);
		if (sb == null) {
			Log.e("MusicUtils", "Trying to unbind for unknown Context");
			return;
		}
		cw.unbindService(sb);
		if (sConnectionMap.isEmpty()) {
			// presumably there is nobody interested in the service at this point,
			// so don't hang on to the ServiceConnection
			sService = null;
		}
	}

	public static class ServiceToken {
		ContextWrapper mWrappedContext;

		ServiceToken(final ContextWrapper context) {
			mWrappedContext = context;
		}
	}

	private static class ServiceBinder implements ServiceConnection {
		ServiceConnection mCallback;

		ServiceBinder(final ServiceConnection callback) {
			mCallback = callback;
		}

		@Override
		public void onServiceConnected(final ComponentName className, final android.os.IBinder service) {
			sService = IMediaPlaybackService.Stub.asInterface(service);
			if (mCallback != null) {
				mCallback.onServiceConnected(className, service);
			}
		}

		@Override
		public void onServiceDisconnected(final ComponentName className) {
			if (mCallback != null) {
				mCallback.onServiceDisconnected(className);
			}
			sService = null;
		}
	}

}
