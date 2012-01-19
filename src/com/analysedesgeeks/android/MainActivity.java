package com.analysedesgeeks.android;

import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.ActionBar.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItem;
import android.support.v4.view.ViewPager;

public class MainActivity extends AbstractPodcastActivity {
	private ViewPager mViewPager;

	private TabsAdapter mTabsAdapter;

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home - already at home
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		updatePodcastInfo();
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		final ActionBar.Tab tab1 = getSupportActionBar().newTab().setText(R.string.podcast);
		final ActionBar.Tab tab2 = getSupportActionBar().newTab().setText(R.string.twitter);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mTabsAdapter = new TabsAdapter(this, getSupportActionBar(), mViewPager);

		mTabsAdapter.addTab(tab1, RssListFragment.class);
		mTabsAdapter.addTab(tab2, WebFragment.class);

		if (savedInstanceState != null) {
			getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("index"));
		}

	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("index", getSupportActionBar().getSelectedNavigationIndex());
	}

	public static class TabsAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener, ActionBar.TabListener {
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;

		public TabsAdapter(final FragmentActivity activity, final ActionBar actionBar, final ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mActionBar = actionBar;
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(final ActionBar.Tab tab, final Class<?> clss) {
			mActionBar.addTab(tab.setTabListener(this));
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Fragment getItem(final int position) {
			if (position == 0) {
				return RssListFragment.newInstance();
			} else if (position == 1) {
				return WebFragment.newInstance(Const.ADG_TWITTER_URL);
			} else {
				return null;
			}
		}

		@Override
		public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

		}

		@Override
		public void onPageScrollStateChanged(final int state) {

		}

		@Override
		public void onPageSelected(final int position) {
			mActionBar.setSelectedNavigationItem(position);
		}

		@Override
		public void onTabReselected(final Tab tab, final FragmentTransaction ft) {

		}

		@Override
		public void onTabSelected(final Tab tab, final FragmentTransaction ft) {
			mViewPager.setCurrentItem(tab.getPosition());
		}

		@Override
		public void onTabUnselected(final Tab tab, final FragmentTransaction ft) {

		}

	}
}
