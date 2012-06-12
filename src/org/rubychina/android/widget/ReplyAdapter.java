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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.rubychina.android.R;
import org.rubychina.android.fragment.TopicDetailFragment;
import org.rubychina.android.fragment.TopicDetailFragment.MockReply;
import org.rubychina.android.type.Reply;
import org.rubychina.android.type.Topic;
import org.rubychina.android.util.DateUtil;
import org.rubychina.android.util.HtmlUtil;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReplyAdapter extends ArrayAdapter<Reply> {

	private List<Reply> items;
	private TopicDetailFragment fragment;
	private int replyResource;
	
	public ReplyAdapter(TopicDetailFragment fragment, int topicBodyResource, int replyResource,
			int textViewResourceId, List<Reply> items) {
		super(fragment.getActivity(), replyResource, textViewResourceId, items);
		this.fragment = fragment;
		this.topicBodyResouce = topicBodyResource;
		this.replyResource = replyResource;
		this.items = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(position == 0) {
			topic = ((MockReply) items.get(position)).getTopic();
			initializeTopicBody(topic);
			return body;
		}
		final ViewHolder viewHolder;
		if(convertView == null || convertView.getTag() == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(replyResource, null);
			viewHolder.gravatar = (ImageView) convertView.findViewById(R.id.gravatar);
			viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
			viewHolder.replyAt = (TextView) convertView.findViewById(R.id.reply_at);
			viewHolder.floor = (TextView) convertView.findViewById(R.id.floor);
			viewHolder.body = (TextView) convertView.findViewById(R.id.body);
			viewHolder.forward = (ImageView) convertView.findViewById(R.id.forward);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Reply r = items.get(position);
		fragment.requestUserAvatar(r.getUser(), viewHolder.gravatar, 0);
		viewHolder.userName.setText(r.getUser().getLogin());
		viewHolder.replyAt.setText(" " + howLongAgo(r.getCreatedAt()) + getContext().getString(R.string.fragment_reply));
		viewHolder.floor.setText(position + "" + fragment.getString(R.string.reply_list_unit));
		if(HtmlUtil.existsImg(r.getBodyHTML())) {
			fragment.executeRetrieveSpannedTask(viewHolder.body, r.getBodyHTML());
		} else {
			viewHolder.body.setText(Html.fromHtml(r.getBodyHTML()));
		}
		viewHolder.gravatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fragment.visitUserProfile(r.getUser());
			}
		});
		return convertView;
	}
	
	private class ViewHolder {
		
		public ImageView gravatar;
		public TextView userName;
		public TextView replyAt;
		public TextView floor;
		public TextView body;
		public ImageView forward;
		
	}
	
	private int topicBodyResouce;
	private View body;
	private Topic topic;
	
	private void initializeTopicBody(final Topic topic) {
		if(body == null) {
			body = LayoutInflater.from(getContext()).inflate(topicBodyResouce, null);
		}
		ImageView gravatar = (ImageView) body.findViewById(R.id.gravatar);
		gravatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fragment.visitUserProfile(topic.getUser());
			}
		});
		fragment.requestUserAvatar(topic.getUser(), gravatar, 0);
		TextView title = (TextView) body.findViewById(R.id.title);
		title.setText(topic.getTitle());
		TextView desc = (TextView) body.findViewById(R.id.desc);
		desc.setText(getTopicDesc(topic));
		TextView bodyText = (TextView) body.findViewById(R.id.body);
		fragment.executeRetrieveSpannedTask(bodyText, topic.getBodyHTML());
	}
	
	private String getTopicDesc(Topic t) {
		StringBuilder sb = new StringBuilder();
		sb.append(t.getUser().getLogin()).append(" ");
		sb.append(howLongAgo(t.getCreatedAt()));
		sb.append(getContext().getString(R.string.fragment_at))
			.append(topic.getNodeName())
			.append(getContext().getString(R.string.fragment_created));
		return sb.toString();
	}
	
	private String howLongAgo(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		StringBuilder sb = new StringBuilder();
		try {
			sdf.parse(date);
			long createAtInMillis = sdf.getCalendar().getTimeInMillis();
			long now = System.currentTimeMillis();
			if(DateUtil.compareYear(now, createAtInMillis) != 0) {
				sb.append(DateUtil.compareYear(now, createAtInMillis) + getContext().getString(R.string.fragment_x_years_ago));
				return sb.toString();
			} else if(DateUtil.compareMonth(now, createAtInMillis) != 0) {
				sb.append(DateUtil.compareMonth(now, createAtInMillis) + getContext().getString(R.string.fragment_x_months_ago));
				return sb.toString();
			} else if(DateUtil.compareDay(now, createAtInMillis) != 0) {
				sb.append(DateUtil.compareDay(now, createAtInMillis) + getContext().getString(R.string.fragment_x_days_ago));
				return sb.toString();
			} else if(DateUtil.compareHour(now, createAtInMillis) != 0) {
				sb.append(DateUtil.compareHour(now, createAtInMillis) + getContext().getString(R.string.fragment_x_hours_ago));
				return sb.toString();
			} else if(DateUtil.compareMinute(now, createAtInMillis) != 0) {
				sb.append(DateUtil.compareMinute(now, createAtInMillis) + getContext().getString(R.string.fragment_x_minutes_ago));
				return sb.toString();
			} else if(DateUtil.compareSecond(now, createAtInMillis) != 0) {
				sb.append(DateUtil.compareSecond(now, createAtInMillis) + getContext().getString(R.string.fragment_x_seconds_ago));
				return sb.toString();
			} 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
}
