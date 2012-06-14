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
package org.rubychina.android;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.rubychina.android.api.RCAPIClient;
import org.rubychina.android.api.parser.JSONParser;

import yek.cache.Cache;
import yek.loader.AsyncLoaderEngine;
import yek.loader.BitmapAsyncLoader;
import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class RCApplication extends Application {

	private static final String TAG = "RCApplication";
	private BitmapAsyncLoader mImgLoader;
	private RCAPIClient mAPIClient;
	private Cache mCacher;
	private ThreadPoolExecutor mThreadPoolExecutor;
	private SharedPreferences mPrefs;
	private int screenWidth;
    private int screenHeight;
	
	@Override
	public void onCreate() {
		super.onCreate();
		setupResource();
	}
	
	private void setupResource() {
		mCacher = new Cache(getApplicationContext());
		mThreadPoolExecutor = new ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS, new ArrayBlockingQueue <Runnable>(100) );
		mAPIClient = new RCAPIClient(getApplicationContext(), new JSONParser(), mThreadPoolExecutor, mCacher);
		mImgLoader = new BitmapAsyncLoader(new AsyncLoaderEngine(getApplicationContext(), mThreadPoolExecutor, mCacher));
	}
	
	public RCAPIClient getAPIClient() {
		return this.mAPIClient;
	}
	
	public BitmapAsyncLoader getImgLoader() {
		return this.mImgLoader;
	}
	
	private SharedPreferences getPreference() {
		if(mPrefs == null) {
			mPrefs = getSharedPreferences(Preferences.PREFERENCE_APP, MODE_PRIVATE);
		}
		return this.mPrefs;
	}
	
	public boolean setToken(String token) {
		return Preferences.setToken(getPreference(), token);
	}
	
	public String getToken() {
		return Preferences.getToken(getPreference());
	}
	
	public boolean isLogin() {
		String token = Preferences.getToken(getPreference());
		return !TextUtils.isEmpty(token);
	}
	
	public boolean setPageSize(int size) {
		return Preferences.setPageSize(getPreference(), size);
	}
	
	public int getPageSize() {
		return Preferences.getPageSize(getPreference());
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
	
}
