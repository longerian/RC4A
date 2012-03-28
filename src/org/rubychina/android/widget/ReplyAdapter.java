/*Copyright (C) 2012 Longerian (http://www.longerian.me)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package org.rubychina.android.widget;

import java.util.List;

import org.rubychina.android.R;
import org.rubychina.android.activity.TopicDetailActivity;
import org.rubychina.android.type.Reply;
import org.rubychina.android.util.HtmlUtil;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReplyAdapter extends ArrayAdapter<Reply> {

	private List<Reply> items;
	private TopicDetailActivity activity;
	private int resource;
	
	public ReplyAdapter(TopicDetailActivity activity, int resource,
			int textViewResourceId, List<Reply> items) {
		super(activity, resource, textViewResourceId, items);
		this.activity = activity;
		this.resource = resource;
		this.items = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(resource, null);
			viewHolder.gravatar = (ImageView) convertView.findViewById(R.id.gravatar);
			viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
			viewHolder.floor = (TextView) convertView.findViewById(R.id.floor);
			viewHolder.body = (TextView) convertView.findViewById(R.id.body);
			viewHolder.forward = (ImageView) convertView.findViewById(R.id.forward);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Reply r = items.get(position);
		activity.requestUserAvatar(r.getUser(), viewHolder.gravatar, 0);
		viewHolder.userName.setText(r.getUser().getLogin());
		viewHolder.floor.setText(position + 1 + "" + activity.getString(R.string.reply_list_unit));
		if(HtmlUtil.existsImg(r.getBodyHTML())) {
			activity.executeRetrieveSpannedTask(viewHolder.body, r.getBodyHTML());
		} else {
			viewHolder.body.setText(Html.fromHtml(r.getBodyHTML()));
		}
		viewHolder.gravatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.visitUserProfile(r.getUser());
			}
		});
		return convertView;
	}
	
	private class ViewHolder {
		
		public ImageView gravatar;
		public TextView userName;
		public TextView floor;
		public TextView body;
		public ImageView forward;
		
	}
	
}
