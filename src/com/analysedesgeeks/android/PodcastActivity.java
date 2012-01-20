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
package com.analysedesgeeks.android;

import org.apache.commons.lang.StringEscapeUtils;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.analysedesgeeks.android.rss.FeedItem;

public class PodcastActivity extends AbstractPodcastActivity {

	@InjectExtra(value = Const.EXTRA_POSITION, optional = true)
	private int position;

	@InjectView(R.id.date)
	private TextView date;

	@InjectView(R.id.title)
	private TextView title;

	@InjectView(R.id.description)
	private TextView description;

	@InjectView(R.id.play)
	private ImageButton playButton;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_podcast);

		final FeedItem msg = rssService.getLastFeed().get(position);

		title.setText(msg.title);
		date.setText(msg.formattedDate);

		String descriptionStr = msg.description;
		descriptionStr = StringEscapeUtils.unescapeHtml(descriptionStr);
		descriptionStr = descriptionStr.replace("- ", "<br>- ");
		descriptionStr = descriptionStr.replaceAll("<img src=\"http://feeds.feedburner.com/~r/LanalyseDesGeeks.*height=\"1\" width=\"1\"/>", "");

		description.setText(Html.fromHtml(descriptionStr));

		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				podcastPlayer.setVisibility(View.VISIBLE);
				podcastPlayer.setDisplayedChild(LOADING_INDEX);
				new PlayTask().execute();
			}
		});
	}

	@Override
	public void onRestoreInstanceState(final Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			position = savedInstanceState.getInt(Const.EXTRA_POSITION);
		}
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		outState.putInt(Const.EXTRA_POSITION, position);
		super.onSaveInstanceState(outState);
	}

	private class PlayTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(final Void... params) {
			final FeedItem msg = rssService.getLastFeed().get(position);

			if (mService == null) {
				return null;
			}

			try {
				mService.stop();
				mService.setDescription(msg.title);
				mService.openFile(msg.link);
				mService.play();

				setIntent(new Intent());
			} catch (final Exception ex) {
				Ln.e(ex);
			}

			return null;
		}

		@Override
		protected void onPostExecute(final Void result) {
			super.onPostExecute(result);

			updatePodcastInfo();
		}

	}

}