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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends AbstractPodcastActivity {

	private static final int TWITTER_FRAGMENT_INDEX = 1;

	private ViewPager mViewPager;

	private TabsAdapter mTabsAdapter;

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		//we need to check if we can go back in the twitter fragment. Otherwise, just pass the event down 

		final ActionBar actionBar = getSupportActionBar();
		if (actionBar.getSelectedNavigationIndex() == TWITTER_FRAGMENT_INDEX) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (keyCode)
				{
				case KeyEvent.KEYCODE_BACK:
					final WebFragment webFragment = (WebFragment) mTabsAdapter.getItem(TWITTER_FRAGMENT_INDEX);
					if (webFragment.goBack()) {
						return true;
					}
				}

			}
		}
		return super.onKeyDown(keyCode, event);
	}

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
		private final Fragment rssFragment;
		private final Fragment twitterFragment;

		public TabsAdapter(final FragmentActivity activity, final ActionBar actionBar, final ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mActionBar = actionBar;
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);

			rssFragment = RssListFragment.newInstance();
			twitterFragment = WebFragment.newInstance(Const.ADG_TWITTER_URL);
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
				return rssFragment;
			} else if (position == 1) {
				return twitterFragment;
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
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
			mViewPager.setCurrentItem(tab.getPosition());
        }

		@Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	       
        }

		@Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
	        
        }
	}
}
