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
		GMT_DATE_PARSER("EEE, d MMM yyyy HH:mm:ss Z");

		private final SimpleDateFormat formater;

		private Parser(final String pattern) {
			formater = new SimpleDateFormat(pattern, Locale.ENGLISH);
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
