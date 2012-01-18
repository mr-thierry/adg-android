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

public class PodcastActivity extends BaseAbstractActivity {

	@InjectExtra(Const.EXTRA_POSITION)
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