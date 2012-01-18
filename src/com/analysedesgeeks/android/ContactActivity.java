package com.analysedesgeeks.android;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ContactActivity extends BaseActivity {

	@InjectView(R.id.contactEmailButton)
	private Button contactEmailButton;

	@InjectView(R.id.contactMarketButton)
	private Button contactMarketButton;

	@InjectView(R.id.contactSupportButton)
	private Button contactSupportButton;

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

		setContentView(R.layout.activity_contact);

		contactEmailButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				ActivityController.onContactEmailClicked(ContactActivity.this);
			}
		});

		contactMarketButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				ActivityController.onContactMartketClicked(ContactActivity.this);
			}
		});

		contactSupportButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				ActivityController.onContactSupportClicked(ContactActivity.this);
			}
		});
	}

}