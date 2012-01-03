package com.analysedesgeeks.android.service;

import java.util.List;

import com.analysedesgeeks.android.data.RssItem;

public interface RssService {

	List<RssItem> getLastFeed();

	List<RssItem> parseRss(String rss);

}
