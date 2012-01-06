package com.analysedesgeeks.android.service;

import java.util.List;

import com.analysedesgeeks.android.rss.FeedItem;

public interface RssService {

	List<FeedItem> getLastFeed();

	List<FeedItem> parseRss(String rss);

}
