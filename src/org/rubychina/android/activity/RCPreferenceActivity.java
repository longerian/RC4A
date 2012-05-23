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
package org.rubychina.android.activity;

import org.rubychina.android.R;
import org.rubychina.android.RCApplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;

public class RCPreferenceActivity extends SherlockActivity {

	private static final String TAG = "UserVerificationActivity";
	private EditText tokenEdit;
	private EditText pageSizeEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.title_preference);
		setContentView(R.layout.rc_preference_layout);

		tokenEdit = (EditText) findViewById(R.id.token);
		tokenEdit.setText(((RCApplication) getApplication()).getToken());
		
		pageSizeEdit = (EditText) findViewById(R.id.page_size);
		pageSizeEdit.setText(((RCApplication) getApplication()).getPageSize() + "");
		
		Button saveBtn = (Button) findViewById(R.id.save);
		saveBtn.setOnClickListener(mSaveListener);
	}
	
	private OnClickListener mSaveListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(isInputValid()) {
				((RCApplication) getApplication()).setToken(tokenEdit.getText().toString());
				((RCApplication) getApplication()).setPageSize(Integer.valueOf(pageSizeEdit.getText().toString()));
				finish();
			}
		}
		
	};
	
	private boolean isInputValid() {
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
//		if(TextUtils.isEmpty(tokenEdit.getText().toString())) {
//			tokenEdit.startAnimation(animation);
//			return false;
//		}
		if(TextUtils.isEmpty(pageSizeEdit.getText().toString())) {
			pageSizeEdit.startAnimation(animation);
			return false;
		} else {
			int size = Integer.valueOf(pageSizeEdit.getText().toString());
			if(size > 100 || size < 1) {
				pageSizeEdit.startAnimation(animation);
				return false;
			}
		}
		return true;
	}
	
}
