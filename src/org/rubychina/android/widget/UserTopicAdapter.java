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
import org.rubychina.android.fragment.TopicActor;
import org.rubychina.android.type.Topic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserTopicAdapter extends TopicAdapter {

	public UserTopicAdapter(TopicActor topicListFragment, Context context,
			int resource, int textViewResourceId, List<Topic> items) {
		super(topicListFragment, context, resource, textViewResourceId, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(resource, null);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.replies = (TextView) convertView.findViewById(R.id.reply_count);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Topic t = items.get(position);
		viewHolder.title.setText(t.getTitle());
		if(t.getRepliesCount() > 99) {
			viewHolder.replies.setText("99+");
		} else {
			viewHolder.replies.setText(t.getRepliesCount() + "");
		}
		return convertView;
	}
	
	private class ViewHolder {
		
		public TextView title;
		public TextView replies;
		
	}
	
}
