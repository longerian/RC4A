package org.rubychina.android.widget;

import org.rubychina.android.type.Section;

import android.content.Context;
import android.widget.ArrayAdapter;

public class NodeSectionHeaderAdapter extends ArrayAdapter<Section> {

	public NodeSectionHeaderAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

}
