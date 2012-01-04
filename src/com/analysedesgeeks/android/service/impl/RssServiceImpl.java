package com.analysedesgeeks.android.service.impl;

import java.util.List;

import com.analysedesgeeks.android.rss.AndroidSaxFeedParser;
import com.analysedesgeeks.android.rss.Message;
import com.analysedesgeeks.android.service.RssService;
import com.google.inject.Singleton;

@Singleton
public class RssServiceImpl implements RssService {

	private List<Message> feed = null;

	@Override
	public List<Message> getLastFeed() {
		return feed;
	}

	@Override
	public List<Message> parseRss(final String rss) {
		feed = new AndroidSaxFeedParser(rss).parse();

		return feed;
	}

}
