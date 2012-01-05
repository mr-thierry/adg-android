package com.analysedesgeeks.android.utils;


public final class Utils {

	public static boolean isEmpty(final CharSequence val) {
		return !isNotEmpty(val);
	}

	public static boolean isNotEmpty(final CharSequence string) {
		if (string == null) {
			return false;
		} else {
			return string.toString().trim().length() > 0;
		}
	}

	public static boolean isNotEmpty(final String string) {
		if (string == null) {
			return false;
		} else {
			return string.trim().length() > 0;
		}
	}

	private Utils() {

	}

}
