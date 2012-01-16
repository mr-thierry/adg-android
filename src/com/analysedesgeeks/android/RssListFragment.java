package com.analysedesgeeks.android;

import java.util.List;

import roboguice.RoboGuice;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.analysedesgeeks.android.rss.FeedItem;
import com.analysedesgeeks.android.service.RssService;
import com.google.inject.Inject;

public class RssListFragment extends ListFragment {

	static RssListFragment newInstance() {
		return new RssListFragment();
	}

	private RssFeedAdapter mAdapter;

	@Inject
	private RssService rssService;

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setEmptyText(getString(R.string.noContentFound));

		setHasOptionsMenu(false);

		mAdapter = new RssFeedAdapter(getActivity());
		setListAdapter(mAdapter);

		setListShown(true);

		// RoboGuice injection via Composition
		RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(this);

		final List<FeedItem> syndFeed = rssService.getLastFeed();
		if (syndFeed != null) {
			mAdapter.setData(syndFeed);
		}

	}

	@Override
	public void onListItemClick(final ListView l, final View v, final int position, final long id) {
		ActivityController.showPodcast(this.getActivity().getApplicationContext(), position);
	}

}
