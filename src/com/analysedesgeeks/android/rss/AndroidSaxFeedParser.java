package com.analysedesgeeks.android.rss;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import roboguice.util.Ln;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import com.analysedesgeeks.android.utils.DateUtils;

public class AndroidSaxFeedParser {

	static final String RSS = "rss";

	private final String feedString;

	// names of the XML tags
	static final String CHANNEL = "channel";
	static final String PUB_DATE = "pubDate";
	static final String DESCRIPTION = "description";
	static final String LINK = "link";
	static final String TITLE = "title";
	static final String ITEM = "item";

	public AndroidSaxFeedParser(final String feedString) {
		this.feedString = feedString;
	}

	public List<Message> parse() {
		final Message currentMessage = new Message();
		final RootElement root = new RootElement(RSS);
		final List<Message> messages = new ArrayList<Message>();
		final Element channel = root.getChild(CHANNEL);
		final Element item = channel.getChild(ITEM);
		item.setEndElementListener(new EndElementListener() {
			@Override
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		item.getChild(TITLE).setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(final String body) {
				currentMessage.title = body;
			}
		});
		item.getChild(LINK).setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(final String body) {
				currentMessage.link = body;
			}
		});
		item.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(final String body) {
				currentMessage.description = body;
			}
		});
		item.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(final String body) {
				try {
					currentMessage.date = DateUtils.Parser.GMT_DATE_PARSER.parse(body);
				} catch (final ParseException e) {
					Ln.e(e);
				}
			}
		});
		try {

			final InputStream is = new ByteArrayInputStream(feedString.getBytes("UTF-8"));

			Xml.parse(is, Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		return messages;
	}
}
