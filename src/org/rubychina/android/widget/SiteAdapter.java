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
import org.rubychina.android.fragment.SiteListFragment;
import org.rubychina.android.type.Site;
import org.rubychina.android.type.SiteGroup;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SiteAdapter extends BaseExpandableListAdapter {

	private List<SiteGroup> site;
	private SiteListFragment fragment;
	private int groupRes;
	private int childRes;
	
	public SiteAdapter(SiteListFragment fragment, List<SiteGroup> site, int groupRes, int childRes) {
		super();
		this.site = site;
		this.fragment = fragment;
		this.groupRes = groupRes;
		this.childRes = childRes;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return site.get(groupPosition).getSites().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition * 10 + childPosition + 1;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ChildViewHolder();
			convertView = LayoutInflater.from(fragment.getActivity()).inflate(childRes, null);
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.split = (ImageView) convertView.findViewById(R.id.split);
			viewHolder.forward = (ImageView) convertView.findViewById(R.id.forward);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ChildViewHolder) convertView.getTag();
		}
		final Site s = (Site) getChild(groupPosition, childPosition);
		viewHolder.name.setText(s.getName());
		fragment.requestImage(s.getFavicon(), viewHolder.icon);
		if(TextUtils.isEmpty(s.getDesc())) {
			viewHolder.split.setVisibility(View.INVISIBLE);
			viewHolder.forward.setVisibility(View.INVISIBLE);
			viewHolder.forward.setOnClickListener(null);
		} else {
			viewHolder.split.setVisibility(View.VISIBLE);
			viewHolder.forward.setVisibility(View.VISIBLE);
			viewHolder.forward.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					fragment.getListener().onSiteDescSelected(s);
				}
			});
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return site.get(groupPosition).getSites().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return site.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return site.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition * 10;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new GroupViewHolder();
			convertView = LayoutInflater.from(fragment.getActivity()).inflate(groupRes, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.desc = (TextView) convertView.findViewById(R.id.desc);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (GroupViewHolder) convertView.getTag();
		}
		SiteGroup sg = (SiteGroup) getGroup(groupPosition);
		viewHolder.name.setText(sg.getName());
		viewHolder.desc.setText(sg.getSites().size() + fragment.getResources().getString(R.string.fragment_x_sites));
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	private class GroupViewHolder {
		
		public TextView name;
		public TextView desc;
		
	}
	
	private class ChildViewHolder {
		
		public ImageView icon;
		public TextView name;
		public ImageView split;
		public ImageView forward;
		
	}

}
