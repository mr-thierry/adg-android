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
