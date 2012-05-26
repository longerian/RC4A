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
package org.rubychina.android.fragment;

import org.rubychina.android.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class ContentDialogFragment extends SherlockDialogFragment {

	private final static String CONTENT = "content";
	
	private String content;
	
	public static ContentDialogFragment newInstance(String content) {
		ContentDialogFragment f = new ContentDialogFragment();
		Bundle bundle = new Bundle();
	    bundle.putString(CONTENT, content);
	    f.setArguments(bundle);
        return f;
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
        	content = getArguments().getString(CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_dialog, container, false);
        View tv = v.findViewById(R.id.content);
        ((TextView)tv).setText(content);
        return v;
    }
    
}
