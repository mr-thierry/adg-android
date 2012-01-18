package com.analysedesgeeks.android;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactActivity extends RoboActivity {

	private static final String REALISED_BY_HTML = "Réalisé par <a href=\"https://plus.google.com/105344175486242358933\">Thierry-Dimitri Roy</a> <br/><br/> Merci à Vincent Miller (<a href=\"http://www.vmiller.ca\">vmiller.ca</a>) pour l'inspiration ;)";

	@InjectView(R.id.moogAudioIV)
	private ImageView moogAudioIV;

	@InjectView(R.id.cipcIV)
	private ImageView cipcIV;

	@InjectView(R.id.aboutTV)
	private TextView aboutTV;

	@Override
	public boolean onOptionsItemSelected(final android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			ActivityController.showMainActivity(this);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_about);

		moogAudioIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				ActivityController.onUrlClicked(ContactActivity.this, Const.MOOG_AUDIO_URL);
			}
		});

		cipcIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				ActivityController.onUrlClicked(ContactActivity.this, Const.CIPC_URL);
			}
		});

		aboutTV.setText(Html.fromHtml(REALISED_BY_HTML));
		Linkify.addLinks(aboutTV, 0);
		aboutTV.setMovementMethod(LinkMovementMethod.getInstance());

	}

}