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
import android.widget.TextView;

public class SourceCodeActivity extends BaseDefaultActivity {

	@InjectView(R.id.sourceCodeDesc)
	private TextView sourceCodeDescTV;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_source_code);

		sourceCodeDescTV.setText(Html.fromHtml(getString(R.string.sourceCodeDesc)));

		Linkify.addLinks(sourceCodeDescTV, 0);
		sourceCodeDescTV.setMovementMethod(LinkMovementMethod.getInstance());
	}

}