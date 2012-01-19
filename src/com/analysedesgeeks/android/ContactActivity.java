package com.analysedesgeeks.android;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ContactActivity extends BaseDefaultActivity {

	@InjectView(R.id.contactEmailButton)
	private Button contactEmailButton;

	@InjectView(R.id.contactMarketButton)
	private Button contactMarketButton;

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

	}

}