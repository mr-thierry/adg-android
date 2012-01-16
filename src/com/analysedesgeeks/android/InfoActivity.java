package com.analysedesgeeks.android;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.analysedesgeeks.android.InfoAdapter.InfoItem;

public class InfoActivity extends RoboActivity {

	@InjectView(R.id.list)
	private ListView listView;

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
		case 1:
			showContact();
		case 2:
			showWeb();
		case 3:
			showFacebook();
		case 4:
			showForum();
		case 5:
			showSend();
			break;

		default:
			break;
		}

	}

	private List<InfoItem> initList() {
		final List<InfoItem> list = new ArrayList<InfoAdapter.InfoItem>();

		list.add(new InfoItem(R.drawable.ic_menu_support, R.string.support));
		list.add(new InfoItem(R.drawable.ic_menu_contact, R.string.about));
		list.add(new InfoItem(R.drawable.ic_menu_web, R.string.web));
		list.add(new InfoItem(R.drawable.ic_menu_facebook, R.string.facebook));
		list.add(new InfoItem(R.drawable.ic_menu_forum, R.string.forum));
		list.add(new InfoItem(R.drawable.ic_menu_send, R.string.contact));

		return list;
	}

	private void showContact() {

	}

	private void showFacebook() {
		// TODO Auto-generated method stub

	}

	private void showForum() {
		// TODO Auto-generated method stub

	}

	private void showSend() {
		// TODO Auto-generated method stub

	}

	private void showSupport() {
		ActivityController.showSupportPAge(this);

	}

	private void showWeb() {
		// TODO Auto-generated method stub

	}
}