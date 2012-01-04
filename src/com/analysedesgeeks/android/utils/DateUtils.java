package com.analysedesgeeks.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

	static {
		final TimeZone tz = TimeZone.getDefault();

		for (final Parser parser : Parser.values()) {
			parser.formater.setTimeZone(tz);
		}

		for (final Formatter df : Formatter.values()) {
			df.formatter.setTimeZone(tz);
		}
	}

	public enum Formatter {
		FULL_DATE_FORMATTER("yyyy-MM-dd HH:mm:ss");

		private final SimpleDateFormat formatter;

		private Formatter(final String pattern) {
			formatter = new SimpleDateFormat(pattern, Locale.FRENCH);
		}

		public String format(final Date d) {
			return formatter.format(d);
		}
	}

	public enum Parser {
		GMT_DATE_PARSER("EEE, dd MMM yyyy HH:mm:ss ZZZZ");

		private final SimpleDateFormat formater;

		private Parser(final String pattern) {
			formater = new SimpleDateFormat(pattern);
		}

		public String format(final Date date) {
			return formater.format(date);
		}

		public Date parse(final String source) throws ParseException {
			if (source == null) {
				throw new ParseException("source is null", 0);
			}

			return formater.parse(source);
		}
	}
}
