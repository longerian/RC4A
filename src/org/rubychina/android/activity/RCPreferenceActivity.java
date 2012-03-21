package org.rubychina.android.activity;

import greendroid.app.GDActivity;

import org.rubychina.android.R;
import org.rubychina.android.RCApplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RCPreferenceActivity extends GDActivity {

	private static final String TAG = "UserVerificationActivity";
	private EditText tokenEdit;
	private EditText pageSizeEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setActionBarContentView(R.layout.rc_preference_layout);

		tokenEdit = (EditText) findViewById(R.id.token);
		tokenEdit.setText(((RCApplication) getApplication()).getToken());
		
		pageSizeEdit = (EditText) findViewById(R.id.page_size);
		pageSizeEdit.setText(((RCApplication) getApplication()).getPageSize() + "");
		
		Button saveBtn = (Button) findViewById(R.id.save);
		saveBtn.setOnClickListener(mVerificationListener);
	}
	
	private OnClickListener mVerificationListener = new OnClickListener() {
		
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
		if(TextUtils.isEmpty(tokenEdit.getText().toString())) {
			tokenEdit.startAnimation(animation);
			return false;
		}
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
