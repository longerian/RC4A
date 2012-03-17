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

public class RCApplication extends GDApplication {

	private static final String TAG = "RCApplication";
	private BitmapAsyncLoader mImgLoader;
	private RCAPIClient mAPIClient;
	private Cache mCacher;
	private ThreadPoolExecutor mThreadPoolExecutor;
	private SharedPreferences mPrefs;
	
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
	
	public SharedPreferences getPreference() {
		if(mPrefs == null) {
			mPrefs = getSharedPreferences(Preferences.PREFERENCE_APP, MODE_PRIVATE);
		}
		return this.mPrefs;
	}
	
}
