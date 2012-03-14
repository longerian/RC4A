package org.rubychina.android.activity;

import org.rubychina.android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;

public class RCActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rc_layout);
	}

	@Override
	protected void onResume() {
		super.onResume();
		new CountDownTimer(1500, 500) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				
			}
			
			@Override
			public void onFinish() {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), TopicsActivity.class);
				startActivity(i);
				finish();
			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

}
