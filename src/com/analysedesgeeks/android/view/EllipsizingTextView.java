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
package com.analysedesgeeks.android.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class EllipsizingTextView extends TextView {
	private static final String ELLIPSIS = "...";

	private final List<EllipsizeListener> ellipsizeListeners = new ArrayList<EllipsizeListener>();

	private boolean isEllipsized;
	private boolean isStale;
	private boolean programmaticChange;
	private String fullText;
	private float lineSpacingMultiplier = 1.0f;
	private float lineAdditionalVerticalPadding = 0.0f;

	private MaxLinesHolder maxLinesHolder;

	public EllipsizingTextView(final Context context) {
		super(context);

		if (maxLinesHolder == null) {
			maxLinesHolder = new MaxLinesHolder();
		}
	}

	public EllipsizingTextView(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		if (maxLinesHolder == null) {
			maxLinesHolder = new MaxLinesHolder();
		}
	}

	public EllipsizingTextView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);

		if (maxLinesHolder == null) {
			maxLinesHolder = new MaxLinesHolder();
		}
	}

	public void addEllipsizeListener(final EllipsizeListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
		ellipsizeListeners.add(listener);
	}

	public int getMaxLines() {
		if (maxLinesHolder == null) {
			maxLinesHolder = new MaxLinesHolder();
		}
		return maxLinesHolder.getLineValue();
	}

	public boolean isEllipsized() {
		return isEllipsized;
	}

	public void removeEllipsizeListener(final EllipsizeListener listener) {
		ellipsizeListeners.remove(listener);
	}

	@Override
	public void setEllipsize(final TruncateAt where) {
		// Ellipsize settings are not respected
	}

	@Override
	public void setLineSpacing(final float add, final float mult) {
		this.lineAdditionalVerticalPadding = add;
		this.lineSpacingMultiplier = mult;
		super.setLineSpacing(add, mult);
	}

	@Override
	public void setMaxLines(final int maxLines) {
		super.setMaxLines(maxLines);
		if (maxLinesHolder == null) {
			maxLinesHolder = new MaxLinesHolder();
		}
		maxLinesHolder.setValue(maxLines);
		isStale = true;
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		if (isStale) {
			super.setEllipsize(null);
			resetText();
		}
		super.onDraw(canvas);
	}

	@Override
	protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
		super.onTextChanged(text, start, before, after);
		if (!programmaticChange) {
			fullText = text.toString();
			isStale = true;
		}
	}

	private Layout createWorkingLayout(final String workingText) {
		return new StaticLayout(workingText, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(),
		        Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineAdditionalVerticalPadding, false);
	}

	private void resetText() {
		String workingText = fullText;
		boolean ellipsized = false;

		if (maxLinesHolder == null) {
			maxLinesHolder = new MaxLinesHolder();
		}

		final int maxLines = maxLinesHolder.getLineValue();
		if (maxLines != -1) {
			final Layout layout = createWorkingLayout(workingText);
			if (layout.getLineCount() > maxLines) {
				workingText = fullText.substring(0, layout.getLineEnd(maxLines - 1)).trim();
				while (createWorkingLayout(workingText + ELLIPSIS).getLineCount() > maxLines) {
					final int lastSpace = workingText.lastIndexOf(' ');
					if (lastSpace == -1) {
						break;
					}
					workingText = workingText.substring(0, lastSpace);
				}
				workingText = workingText + ELLIPSIS;
				ellipsized = true;
			}
		}
		if (!workingText.equals(getText())) {
			programmaticChange = true;
			try {
				setText(workingText);
			} finally {
				programmaticChange = false;
			}
		}
		isStale = false;
		if (ellipsized != isEllipsized) {
			isEllipsized = ellipsized;
			for (final EllipsizeListener listener : ellipsizeListeners) {
				listener.ellipsizeStateChanged(ellipsized);
			}
		}
	}

	public interface EllipsizeListener {
		void ellipsizeStateChanged(boolean ellipsized);
	}

	private static class MaxLinesHolder {
		private static final Integer[] holder = new Integer[] { 0 };

		public int getLineValue() {
			return holder[0];
		}

		public void setValue(final int value) {
			MaxLinesHolder.holder[0] = value;
		}
	}
}
