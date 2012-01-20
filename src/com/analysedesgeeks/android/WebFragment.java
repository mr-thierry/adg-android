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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebFragment extends Fragment {

	protected static final String URL = "URL";

	static WebFragment newInstance(final String url) {
		final WebFragment f = new WebFragment();

		final Bundle args = new Bundle();
		args.putString(URL, url);
		f.setArguments(args);

		return f;
	}

	private String url;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		url = getArguments() != null ? getArguments().getString(URL) : null;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
	        final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		final View v = inflater.inflate(R.layout.fragment_webview, container, false);

		final WebView webview = (WebView) v.findViewById(R.id.webview);

		final WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);

		final View loading = v.findViewById(R.id.loading);

		webview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(final WebView view, final String url) {
				loading.setVisibility(View.GONE);
			}
		});

		webview.loadUrl(url);

		return v;
	}
}
