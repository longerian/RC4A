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
import org.rubychina.android.activity.RubyChinaActor;
import org.rubychina.android.type.User;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class UserProfileFragment extends SherlockFragment {

	public static final String USER = "user"; 
	public static final String DENSITY = "density"; 
	
	private RubyChinaActor rubyChina;
	
	private User user;
	private float density;
	
	public static UserProfileFragment newInstance(User user, float density) {
		UserProfileFragment f = new UserProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(USER, user);
        bundle.putFloat(DENSITY, density);
        f.setArguments(bundle);
        return f;
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	rubyChina = (RubyChinaActor) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement RubyChinaActor");
        }
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
        	user = getArguments().getParcelable(USER);
        	density = getArguments().getFloat(DENSITY);
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.user_profile_layout, null);
    	TextView login = (TextView) view.findViewById(R.id.login);
		login.setText(user.getLogin());
		
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(user.getName());
		
		TextView location = (TextView) view.findViewById(R.id.location);
		location.setText(user.getLocation());
		
		TextView website = (TextView) view.findViewById(R.id.website);
		website.setText(user.getWebsite());
		
		TextView bio = (TextView) view.findViewById(R.id.bio);
		bio.setText(user.getBio());
		
		TextView tagline = (TextView) view.findViewById(R.id.tagline);
		tagline.setText(user.getTagline());
		
		TextView github = (TextView) view.findViewById(R.id.github_url);
		github.setText(user.getGithubUrl());
		
		ImageView gravatar = (ImageView) view.findViewById(R.id.gravatar);
		rubyChina.getService().requestUserAvatar(user, gravatar, (int) (96 * density));
		
		return view;
	}
	
}
