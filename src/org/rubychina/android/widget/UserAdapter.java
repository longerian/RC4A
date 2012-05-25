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
import org.rubychina.android.fragment.UserListFragment;
import org.rubychina.android.type.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserAdapter extends ArrayAdapter<User> {

	private List<User> items;
	private UserListFragment fragment;
	private int resource;
	
	public UserAdapter(UserListFragment userListFragment, int resource,
			int textViewResourceId, List<User> items) {
		super(userListFragment.getActivity(), resource, textViewResourceId, items);
		this.fragment = userListFragment;
		this.resource = resource;
		this.items = items;
	}
	
	public List<User> getItems() {
		return items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(fragment.getActivity()).inflate(resource, null);
			viewHolder.gravatar = (ImageView) convertView.findViewById(R.id.gravatar);
			viewHolder.login = (TextView) convertView.findViewById(R.id.login);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		User u = items.get(position);
		fragment.requestUserAvatar(u, viewHolder.gravatar);
		viewHolder.login.setText(u.getLogin());
		return convertView;
	}
	
	private class ViewHolder {
		
		public ImageView gravatar;
		public TextView login;
		
	}
	
}
