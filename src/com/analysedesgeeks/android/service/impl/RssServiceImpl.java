package com.analysedesgeeks.android.service.impl;

import java.util.List;

import com.analysedesgeeks.android.rss.AndroidSaxFeedParser;
import com.analysedesgeeks.android.rss.FeedItem;
import com.analysedesgeeks.android.service.RssService;
import com.google.inject.Singleton;

@Singleton
public class RssServiceImpl implements RssService {

	private List<FeedItem> feed = null;

	@Override
	public List<FeedItem> getLastFeed() {
		return feed;
	}

	@Override
	public List<FeedItem> parseRss(final String rss) {
		feed = new AndroidSaxFeedParser(rss).parse();

		return feed;
	}

}
