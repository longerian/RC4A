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
import org.rubychina.android.RCService;
import org.rubychina.android.RCService.LocalBinder;
import org.rubychina.android.type.User;
import org.rubychina.android.util.JsonUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

@Deprecated
public class UserProfileActivity extends SherlockActivity {
	
	private static final String VIEW_PROFILE = "org.rubychina.android.activity.UserProfileActivity.VIEW_PROFILE";

	private RCService mService;
	private boolean isBound = false; 
	private DisplayMetrics metrics;
	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		setTitle(R.string.title_profile);
		
		setContentView(R.layout.user_profile_layout);
		
		if(getIntent().getStringExtra(VIEW_PROFILE) != null) {
			user = JsonUtil.fromJsonObject(getIntent().getStringExtra(VIEW_PROFILE), User.class);
		} else {
			user = getIntent().getExtras().getParcelable(VIEW_PROFILE);
		}
		
		
		TextView login = (TextView) findViewById(R.id.login);
		login.setText(user.getLogin());
		
		TextView name = (TextView) findViewById(R.id.name);
		name.setText(user.getName());
		
		TextView location = (TextView) findViewById(R.id.location);
		location.setText(user.getLocation());
		
		TextView website = (TextView) findViewById(R.id.website);
		website.setText(user.getWebsite());
		
		TextView bio = (TextView) findViewById(R.id.bio);
		bio.setText(user.getBio());
		
		TextView tagline = (TextView) findViewById(R.id.tagline);
		tagline.setText(user.getTagline());
		
		TextView github = (TextView) findViewById(R.id.github_url);
		github.setText(user.getGithubUrl());

	}
	
	@Override
	protected void onStart() {
		super.onStart();
        Intent intent = new Intent(this, RCService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            isBound = true;
            requestUserAvatar();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };
    
    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }
    
    private void requestUserAvatar() {
		ImageView gravatar = (ImageView) findViewById(R.id.gravatar);
		mService.requestUserAvatar(user, gravatar, (int) (96 * metrics.density));
	}
	
}
