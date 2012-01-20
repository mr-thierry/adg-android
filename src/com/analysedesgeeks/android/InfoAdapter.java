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

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoAdapter extends BaseAdapter {

	private final List<InfoItem> list;

	protected final LayoutInflater inflater;

	protected Context context;

	private final Resources resources;

	public InfoAdapter(final Context context, final List<InfoItem> list) {
		super();

		this.list = list;
		this.context = context.getApplicationContext();
		this.resources = context.getResources();
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public InfoItem getItem(final int position) {
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
			view = inflater.inflate(R.layout.widget_info_item, null);

			holder = new ViewHolder();

			holder.icon = (ImageView) view.findViewById(R.id.icon);
			holder.text = (TextView) view.findViewById(R.id.text);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final InfoItem item = getItem(position);
		holder.icon.setImageResource(item.iconRes);
		holder.text.setText(resources.getString(item.textRes));

		return view;
	}

	static class InfoItem {
		private final int iconRes;
		private final int textRes;

		public InfoItem(final int iconRes, final int textRes) {
			super();
			this.iconRes = iconRes;
			this.textRes = textRes;
		}

	}

	static class ViewHolder {

		public ImageView icon;
		public TextView text;

	}

}
