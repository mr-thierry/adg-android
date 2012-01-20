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

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.analysedesgeeks.android.InfoAdapter.InfoItem;

public class InfoActivity extends BaseDefaultActivity {

	@InjectView(R.id.list)
	private ListView listView;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_info);

		final List<InfoItem> list = initList();

		listView.setAdapter(new InfoAdapter(this, list));

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> arg0, final View arg1, final int position, final long arg3) {
				handleItemClicked(position);
			}
		});

	}

	private void handleItemClicked(final int position) {
		switch (position) {
		case 0:
			showSupport();
			break;
		case 1:
			showAbout();
			break;
		case 2:
			showWeb();
			break;
		case 3:
			showFacebook();
			break;
		case 4:
			showForum();
			break;
		case 5:
			showContact();
			break;
		case 6:
			showSource();
			break;

		default:
			break;
		}

	}

	private List<InfoItem> initList() {
		final List<InfoItem> list = new ArrayList<InfoAdapter.InfoItem>();

		list.add(new InfoItem(R.drawable.ic_menu_support, R.string.support));
		list.add(new InfoItem(R.drawable.ic_menu_about, R.string.about));
		list.add(new InfoItem(R.drawable.ic_menu_web, R.string.web));
		list.add(new InfoItem(R.drawable.ic_menu_facebook, R.string.facebook));
		list.add(new InfoItem(R.drawable.ic_menu_forum, R.string.forum));
		list.add(new InfoItem(R.drawable.ic_menu_contact, R.string.contact));
		list.add(new InfoItem(R.drawable.ic_menu_source, R.string.sourceCode));

		return list;
	}

	private void showAbout() {
		ActivityController.showAboutPage(this);
	}

	private void showContact() {
		ActivityController.showContact(this);
	}

	private void showFacebook() {
		ActivityController.showFacebook(this);
	}

	private void showForum() {
		ActivityController.showForum(this);
	}

	private void showSource() {
		ActivityController.showSource(this);
	}

	private void showSupport() {
		ActivityController.showSupportPage(this);
	}

	private void showWeb() {
		ActivityController.showWebPage(this);
	}
}