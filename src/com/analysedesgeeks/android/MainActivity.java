package com.analysedesgeeks.android;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ViewSwitcher;

import com.analysedesgeeks.android.rss.FeedItem;
import com.analysedesgeeks.android.service.RssService;
import com.google.inject.Inject;

public class MainActivity extends RoboActivity {

	private static final int CONTENT_IDX = 1;

	@InjectView(R.id.list)
	private ListView list;

	@InjectView(R.id.viewSwitcher)
	private ViewSwitcher viewSwitcher;

	@Inject
	private RssService rssService;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final RssFeedAdapter adapter = new RssFeedAdapter(this);
		list.setAdapter(adapter);

		final List<FeedItem> syndFeed = rssService.getLastFeed();
		if (syndFeed != null) {
			adapter.setData(syndFeed);
			viewSwitcher.setDisplayedChild(CONTENT_IDX);
		}

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				ActivityController.showPodcast(MainActivity.this, position);

			}
		});
	}

}