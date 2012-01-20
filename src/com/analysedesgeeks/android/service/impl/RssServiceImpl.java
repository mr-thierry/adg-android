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
