package com.analysedesgeeks.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RssListFragment extends Fragment {
	/**
	 * Create a new instance of RssListFragment, providing "num"
	 * as an argument.
	 */
	static RssListFragment newInstance(final int num) {
		final RssListFragment f = new RssListFragment();

		// Supply num input as an argument.
		final Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);

		return f;
	}

	int mNum;

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments() != null ? getArguments().getInt("num") : 1;
	}

	/**
	 * The Fragment's UI is just a simple text view showing its
	 * instance number.
	 */
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
	        final Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.hello_world, container, false);
		final View tv = v.findViewById(R.id.text);
		((TextView) tv).setText("Fragment #" + mNum);
		tv.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.gallery_thumb));
		return v;
	}
}
