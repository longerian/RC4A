/* Copyright 2008 Jeff Sharkey
*
*  From: http://www.jsharkey.org/blog/2008/08/18/separating-lists-with-headers-in-android-09/
*/

package org.rubychina.android.widget;

import java.util.LinkedHashMap;
import java.util.Map;

import org.rubychina.android.R;
import org.rubychina.android.type.NodeSection;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class SeparatedListAdapter extends BaseAdapter {

	public final Map<NodeSection, Adapter> sections = new LinkedHashMap<NodeSection, Adapter>();
	public final ArrayAdapter<NodeSection> headers;
	public final static int TYPE_SECTION_HEADER = 0;

	public SeparatedListAdapter(Context context) {
		headers = new NodeSectionHeaderAdapter(context, R.layout.node_section_header, R.id.node_section_header_title);
	}

	public void addSection(NodeSection section, Adapter adapter) {
		this.headers.add(section);
		this.sections.put(section, adapter);
	}

	public Object getItem(int position) {
		for(Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if(position == 0) return section;
			if(position < size) return adapter.getItem(position - 1);

			// otherwise jump into next section
			position -= size;
		}
		return null;
	}

	public int getCount() {
		// total together all sections, plus one for each section header
		int total = 0;
		for(Adapter adapter : this.sections.values())
			total += adapter.getCount() + 1;
		return total;
	}

	public int getViewTypeCount() {
		// assume that headers count as one, then total all sections
		int total = 1;
		for(Adapter adapter : this.sections.values())
			total += adapter.getViewTypeCount();
		return total;
	}

	public int getItemViewType(int position) {
		int type = 1;
		for(Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if(position == 0) return TYPE_SECTION_HEADER;
			if(position < size) return type + adapter.getItemViewType(position - 1);

			// otherwise jump into next section
			position -= size;
			type += adapter.getViewTypeCount();
		}
		return -1;
	}

	public boolean areAllItemsSelectable() {
		return false;
	}

	public boolean isEnabled(int position) {
		return (getItemViewType(position) != TYPE_SECTION_HEADER);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int sectionnum = 0;
		for(Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if(position == 0) return headers.getView(sectionnum, convertView, parent);
			if(position < size) return adapter.getView(position - 1, convertView, parent);

			// otherwise jump into next section
			position -= size;
			sectionnum++;
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}