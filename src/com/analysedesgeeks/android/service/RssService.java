package com.analysedesgeeks.android.service;

import java.util.List;

import com.analysedesgeeks.android.rss.Message;

public interface RssService {

	 List<Message> getLastFeed();

	 List<Message> parseRss(String rss);

}
