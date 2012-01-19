package com.analysedesgeeks.android;

import roboguice.activity.RoboFragmentActivity;
import android.support.v4.view.MenuItem;

public abstract class BaseDefaultActivity extends RoboFragmentActivity {

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
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