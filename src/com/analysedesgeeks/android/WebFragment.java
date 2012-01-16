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
