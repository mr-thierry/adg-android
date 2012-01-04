package com.analysedesgeeks.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.analysedesgeeks.android.rss.Message;
import com.analysedesgeeks.android.utils.DateUtils;

public class RssFeedAdapter extends BaseAdapter {

	private final List<Message> list;

	protected final LayoutInflater inflater;

	protected Context context;

	public RssFeedAdapter(final Context context) {
		super();
		this.context = context.getApplicationContext();
		this.list = new ArrayList<Message>();
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Message getItem(final int position) {
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

			holder.title = (TextView) view.findViewById(R.id.title);
			holder.date = (TextView) view.findViewById(R.id.date);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final Message rssItem = getItem(position);
		if (rssItem != null) {
			holder.title.setText(rssItem.title);

			String formattedDate = rssItem.formattedDate;
			if (formattedDate == null) {
				if (rssItem.date != null) {
					rssItem.formattedDate = DateUtils.Formatter.FULL_DATE_FORMATTER.format(rssItem.date);
				} else {
					rssItem.formattedDate = "";
				}
				formattedDate = rssItem.formattedDate;
			}

			holder.date.setText(formattedDate);
		}

		return view;
	}

	public void setData(final List<Message> msgs) {
		if (!list.isEmpty()) {
			list.clear();
		}

		if (msgs.size() > 0) {
			list.addAll(msgs);
		}

		notifyDataSetChanged();
	}

	static class ViewHolder {

		public TextView date;
		public TextView title;

	}

}
