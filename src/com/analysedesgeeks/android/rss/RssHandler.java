package com.analysedesgeeks.android.rss;

import static com.analysedesgeeks.android.rss.AndroidSaxFeedParser.DESCRIPTION;
import static com.analysedesgeeks.android.rss.AndroidSaxFeedParser.ITEM;
import static com.analysedesgeeks.android.rss.AndroidSaxFeedParser.LINK;
import static com.analysedesgeeks.android.rss.AndroidSaxFeedParser.PUB_DATE;
import static com.analysedesgeeks.android.rss.AndroidSaxFeedParser.TITLE;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import roboguice.util.Ln;

import com.analysedesgeeks.android.Const;
import com.analysedesgeeks.android.utils.DateUtils;

public class RssHandler extends DefaultHandler {
	private List<Message> messages;
	private Message currentMessage;
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
			if (this.currentMessage != null) {
				if (localName.equalsIgnoreCase(TITLE)) {
					currentMessage.title = builder.toString();
				} else if (localName.equalsIgnoreCase(LINK)) {
					currentMessage.link = new URL(builder.toString());
				} else if (localName.equalsIgnoreCase(DESCRIPTION)) {
					currentMessage.description = builder.toString();
				} else if (localName.equalsIgnoreCase(PUB_DATE)) {
					currentMessage.date = DateUtils.Parser.GMT_DATE_PARSER.parse(builder.toString());
				} else if (localName.equalsIgnoreCase(ITEM)) {
					messages.add(currentMessage);
				}
				builder.setLength(0);
			}
		} catch (final Exception e) {
			Ln.e(Const.TAG, e);
		}
	}

	public List<Message> getMessages() {
		return this.messages;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		messages = new ArrayList<Message>();
		builder = new StringBuilder();
	}

	@Override
	public void startElement(final String uri, final String localName, final String name,
	        final Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if (localName.equalsIgnoreCase(ITEM)) {
			this.currentMessage = new Message();
		}
	}
}