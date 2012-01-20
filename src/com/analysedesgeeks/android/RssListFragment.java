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
