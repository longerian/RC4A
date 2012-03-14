package org.rubychina.android;

import greendroid.app.GDApplication;

import org.rubychina.android.activity.TopicsActivity;

public class RCApplication extends GDApplication {

	@Override
	public Class<?> getHomeActivityClass() {
		return TopicsActivity.class;
	}

}
