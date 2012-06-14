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
import org.rubychina.android.util.DebugUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class RCPreferenceActivity extends SherlockActivity {

	private static final String TAG = "UserVerificationActivity";
	private EditText tokenEdit;
	private EditText pageSizeEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DebugUtil.setupErrorHandler(this);
		setTitle(R.string.title_preference);
		setContentView(R.layout.rc_preference_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		tokenEdit = (EditText) findViewById(R.id.token);
		tokenEdit.setText(((RCApplication) getApplication()).getToken());
		
		pageSizeEdit = (EditText) findViewById(R.id.page_size);
		pageSizeEdit.setText(((RCApplication) getApplication()).getPageSize() + "");
	}
	
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
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, R.id.action_bar_save, 0, R.string.actionbar_save)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	Intent intent = new Intent(this, RubyChinaIndexActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	            startActivity(intent);
	            return true;
	        case R.id.action_bar_save:
	        	if(isInputValid()) {
					((RCApplication) getApplication()).setToken(tokenEdit.getText().toString());
					((RCApplication) getApplication()).setPageSize(Integer.valueOf(pageSizeEdit.getText().toString()));
					finish();
				}
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
