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
import org.rubychina.android.type.Node;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NodeAdapter extends ArrayAdapter<Node> {

	private List<Node> items;
	private Context context;
	private int resource;
	
	public NodeAdapter(Context context, int resource,
			int textViewResourceId, List<Node> items) {
		super(context, resource, textViewResourceId, items);
		this.context = context;
		this.resource = resource;
		this.items = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(resource, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Node n = items.get(position);
		viewHolder.name.setText(n.getName());
		return convertView;
	}
	
	private class ViewHolder {
		
		public TextView name;
		
	}
	
}
