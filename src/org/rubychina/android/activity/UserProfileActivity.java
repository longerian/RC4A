package org.rubychina.android.activity;

import greendroid.app.GDActivity;

import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.type.User;
import org.rubychina.android.util.GravatarUtil;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class UserProfileActivity extends GDActivity {
	
	public static final String VIEW_PROFILE = "org.rubychina.android.activity.UserProfileActivity.VIEW_PROFILE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setActionBarContentView(R.layout.user_profile_layout);
		
		Gson g = new Gson();
		User u = g.fromJson(getIntent().getStringExtra(VIEW_PROFILE), User.class);
		
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

		ImageView gravatar = (ImageView) findViewById(R.id.gravatar);
		String avatar = u.getAvatarUrl();
		String hash = u.getGravatarHash();
		if(TextUtils.isEmpty(avatar)) {
			Bitmap ava = ((RCApplication) getApplication()).getImgLoader().load(GravatarUtil.getURLWithSize(hash, 96), gravatar);
			if(ava != null) {
				gravatar.setImageBitmap(ava);
			} else {
				gravatar.setImageResource(R.drawable.default_gravatar);
			}
		} else {
			Bitmap ava = ((RCApplication) getApplication()).getImgLoader().load(avatar, gravatar);
			if(ava != null) {
				gravatar.setImageBitmap(ava);
			} else {
				gravatar.setImageResource(R.drawable.default_gravatar);
			}
		}
	}
	
}
