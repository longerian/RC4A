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
