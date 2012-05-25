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
import org.rubychina.android.fragment.TopicListFragment;
import org.rubychina.android.type.Topic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicAdapter extends ArrayAdapter<Topic> {

	private List<Topic> items;
	private TopicListFragment fragment;
	private int resource;
	
	public TopicAdapter(TopicListFragment topicListFragment, int resource,
			int textViewResourceId, List<Topic> items) {
		super(topicListFragment.getActivity(), resource, textViewResourceId, items);
		this.fragment = topicListFragment;
		this.resource = resource;
		this.items = items;
	}
	
	public List<Topic> getItems() {
		return items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(fragment.getActivity()).inflate(resource, null);
			viewHolder.gravatar = (ImageView) convertView.findViewById(R.id.gravatar);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.author = (TextView) convertView.findViewById(R.id.author);
			viewHolder.replies = (TextView) convertView.findViewById(R.id.reply_count);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Topic t = items.get(position);
		fragment.requestUserAvatar(t.getUser(), viewHolder.gravatar, 0);
		viewHolder.title.setText(t.getTitle());
		viewHolder.author.setText(" >> " + t.getUser().getLogin() + " 在  " + t.getNodeName() + " 中发起");
		if(t.getRepliesCount() > 99) {
			viewHolder.replies.setText("99+");
		} else {
			viewHolder.replies.setText(t.getRepliesCount() + "");
		}
		viewHolder.gravatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fragment.visitUserProfile(t.getUser());
			}
		});
		return convertView;
	}
	
	private class ViewHolder {
		
		public ImageView gravatar;
		public TextView title;
		public TextView author;
		public TextView replies;
		
	}
	
}
