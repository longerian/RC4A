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

import org.rubychina.android.type.Section;

import android.content.Context;
import android.widget.ArrayAdapter;

public class NodeSectionHeaderAdapter extends ArrayAdapter<Section> {

	public NodeSectionHeaderAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

}
