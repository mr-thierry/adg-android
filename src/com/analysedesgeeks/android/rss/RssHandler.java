package com.analysedesgeeks.android.rss;

import static com.analysedesgeeks.android.rss.AndroidSaxFeedParser.DESCRIPTION;
import static com.analysedesgeeks.android.rss.AndroidSaxFeedParser.ITEM;
import static com.analysedesgeeks.android.rss.AndroidSaxFeedParser.LINK;
import static com.analysedesgeeks.android.rss.AndroidSaxFeedParser.PUB_DATE;
import static com.analysedesgeeks.android.rss.AndroidSaxFeedParser.TITLE;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import roboguice.util.Ln;

import com.analysedesgeeks.android.utils.DateUtils;

public class RssHandler extends DefaultHandler {
	private List<FeedItem> messages;
	private FeedItem currentFeedItem;
	private StringBuilder builder;

	@Override
	public void characters(final char[] ch, final int start, final int length)
	        throws SAXException {
		super.characters(ch, start, length);
		builder.append(ch, start, length);
	}

	@Override
	public void endElement(final String uri, final String localName, final String name)
	        throws SAXException {
		super.endElement(uri, localName, name);
		try {
			if (this.currentFeedItem != null) {
				if (localName.equalsIgnoreCase(TITLE)) {
					currentFeedItem.title = builder.toString();
				} else if (localName.equalsIgnoreCase(LINK)) {
					currentFeedItem.link = builder.toString();
				} else if (localName.equalsIgnoreCase(DESCRIPTION)) {
					currentFeedItem.description = builder.toString();
				} else if (localName.equalsIgnoreCase(PUB_DATE)) {
					currentFeedItem.date = DateUtils.Parser.GMT_DATE_PARSER.parse(builder.toString());
				} else if (localName.equalsIgnoreCase(ITEM)) {
					messages.add(currentFeedItem);
				}
				builder.setLength(0);
			}
		} catch (final Exception e) {
			Ln.e(e);
		}
	}

	public List<FeedItem> getFeedItems() {
		return this.messages;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		messages = new ArrayList<FeedItem>();
		builder = new StringBuilder();
	}

	@Override
	public void startElement(final String uri, final String localName, final String name,
	        final Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if (localName.equalsIgnoreCase(ITEM)) {
			this.currentFeedItem = new FeedItem();
		}
	}
}