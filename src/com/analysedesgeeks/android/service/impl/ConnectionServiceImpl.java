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
package com.analysedesgeeks.android.service.impl;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import com.analysedesgeeks.android.service.ConnectionService;
import com.google.inject.Inject;

public class ConnectionServiceImpl implements ConnectionService {

	@Inject
	private ConnectivityManager connectivityManager;

	@Override
	public boolean isConnected() {
		final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
		if (ni == null) {
			//sometime when wifi is connecting, ni is null so check wifi explicitly
			final NetworkInfo wifiConnectivityInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiConnectivityInfo.isConnected() || wifiConnectivityInfo.isConnectedOrConnecting()) {
				return true;
			}

			return true;
		} else {
			final State state = ni.getState();
			if (state.equals(State.CONNECTED) || state.equals(State.CONNECTING)) {
				return true;
			}

			return false;
		}
	}

}
