package com.analysedesgeeks.android;

import roboguice.activity.RoboActivity;

public abstract class BaseActivity extends RoboActivity {

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

}