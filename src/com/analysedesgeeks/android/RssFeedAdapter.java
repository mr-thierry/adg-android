package com.analysedesgeeks.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.analysedesgeeks.android.data.RssItem;

public class RssFeedAdapter extends BaseAdapter {

	private final List<RssItem> list;

	protected final LayoutInflater inflater;

	protected Context context;

	public RssFeedAdapter(final Context context) {
		super();
		this.context = context.getApplicationContext();
		this.list = new ArrayList<RssItem>();
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public RssItem getItem(final int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		ViewHolder holder;

		View view = convertView;

		if (view == null) {
			view = inflater.inflate(R.layout.widget_rss_item, null);

			holder = new ViewHolder();

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final RssItem rssItem = getItem(position);
		if (rssItem != null) {

		}

		return view;
	}

	public void setData(final List<RssItem> data) {
		if (!list.isEmpty()) {
			list.clear();
		}

		if (data != null && data.size() > 0) {
			list.addAll(data);
		}
		notifyDataSetChanged();
	}

	static class ViewHolder {

	}

}
