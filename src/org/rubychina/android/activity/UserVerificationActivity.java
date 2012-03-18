package org.rubychina.android.activity;

import greendroid.app.GDActivity;

import org.rubychina.android.R;
import org.rubychina.android.RCApplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserVerificationActivity extends GDActivity {

	private static final String TAG = "UserVerificationActivity";
	private EditText tokenEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setActionBarContentView(R.layout.user_verification_layout);

		tokenEdit = (EditText) findViewById(R.id.token);
		tokenEdit.setText(((RCApplication) getApplication()).getToken());
		
		Button vrfnBtn = (Button) findViewById(R.id.verify);
		vrfnBtn.setOnClickListener(mVerificationListener);
	}
	
	private OnClickListener mVerificationListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(TextUtils.isEmpty(tokenEdit.getText().toString())) {
				Toast.makeText(getApplicationContext(), R.string.hint_no_token, Toast.LENGTH_SHORT).show();
			} else {
				((RCApplication) getApplication()).setToken(tokenEdit.getText().toString());
				finish();
			}
		}
		
	};
	
}
