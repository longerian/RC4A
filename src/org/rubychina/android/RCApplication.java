package org.rubychina.android;

import greendroid.app.GDApplication;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.rubychina.android.activity.TopicsActivity;
import org.rubychina.android.api.RCAPIClient;
import org.rubychina.android.api.parser.JSONParser;

import yek.cache.Cache;
import yek.loader.AsyncLoaderEngine;
import yek.loader.BitmapAsyncLoader;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

public class RCApplication extends GDApplication {

	private static final String TAG = "RCApplication";
	private BitmapAsyncLoader mImgLoader;
	private RCAPIClient mAPIClient;
	private Cache mCacher;
	private ThreadPoolExecutor mThreadPoolExecutor;
	private SharedPreferences mPrefs;
	private int screenWidth;
    private int screenHeight;
	
	@Override
	public Class<?> getHomeActivityClass() {
		return TopicsActivity.class;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setupResource();
		//TODO add uncaught exceptions handler
	}
	
	private void setupResource() {
		mCacher = new Cache(getApplicationContext());
		mThreadPoolExecutor = new ThreadPoolExecutor(1, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue <Runnable>(100) );
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
