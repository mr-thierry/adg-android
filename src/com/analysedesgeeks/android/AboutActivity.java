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

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends BaseDefaultActivity {

	private static final String REALISED_BY_HTML = "Réalisé par <a href=\"https://about.me/thierryd\">Thierry-Dimitri Roy</a> <br/><br/> Merci à Vincent Miller (<a href=\"http://www.vmiller.ca\">vmiller.ca</a>) pour l'inspiration ;)";

	@InjectView(R.id.moogAudioIV)
	private ImageView moogAudioIV;

	@InjectView(R.id.cipcIV)
	private ImageView cipcIV;

	@InjectView(R.id.aboutTV)
	private TextView aboutTV;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_about);

		moogAudioIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				ActivityController.onUrlClicked(AboutActivity.this, Const.MOOG_AUDIO_URL);
			}
		});

		cipcIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				ActivityController.onUrlClicked(AboutActivity.this, Const.CIPC_URL);
			}
		});

		aboutTV.setText(Html.fromHtml(REALISED_BY_HTML));
		Linkify.addLinks(aboutTV, 0);
		aboutTV.setMovementMethod(LinkMovementMethod.getInstance());

	}

}