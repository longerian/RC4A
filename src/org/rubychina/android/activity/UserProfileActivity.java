package org.rubychina.android.activity;

import greendroid.app.GDActivity;

import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.RCService;
import org.rubychina.android.RCService.LocalBinder;
import org.rubychina.android.type.User;
import org.rubychina.android.util.GravatarUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class UserProfileActivity extends GDActivity {
	
	public static final String VIEW_PROFILE = "org.rubychina.android.activity.UserProfileActivity.VIEW_PROFILE";

	private RCService mService;
	private boolean isBound = false; 
	
	private User u;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setActionBarContentView(R.layout.user_profile_layout);
		
		Gson g = new Gson();
		u = g.fromJson(getIntent().getStringExtra(VIEW_PROFILE), User.class);
		
		TextView login = (TextView) findViewById(R.id.login);
		login.setText(u.getLogin());
		
		TextView name = (TextView) findViewById(R.id.name);
		name.setText(u.getName());
		
		TextView location = (TextView) findViewById(R.id.location);
		location.setText(u.getLocation());
		
		TextView website = (TextView) findViewById(R.id.website);
		website.setText(u.getWebsite());
		
		TextView bio = (TextView) findViewById(R.id.bio);
		bio.setText(u.getBio());
		
		TextView tagline = (TextView) findViewById(R.id.tagline);
		tagline.setText(u.getTagline());
		
		TextView github = (TextView) findViewById(R.id.github_url);
		github.setText(u.getGithubUrl());

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
		mService.requestUserAvatar(u, gravatar);
	}
	
}
