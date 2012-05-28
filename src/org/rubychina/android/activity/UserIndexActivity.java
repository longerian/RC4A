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

import java.util.ArrayList;
import java.util.List;

import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.RCService;
import org.rubychina.android.RCService.LocalBinder;
import org.rubychina.android.api.RCAPIClient;
import org.rubychina.android.fragment.UserFavoriteTopicListFragment;
import org.rubychina.android.fragment.UserProfileFragment;
import org.rubychina.android.fragment.UserRecentlyCreatedTopicListFragment;
import org.rubychina.android.fragment.UserRelativeTopicListFragment.OnTopicSelectedListener;
import org.rubychina.android.type.Topic;
import org.rubychina.android.type.User;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

public class UserIndexActivity extends SherlockFragmentActivity implements ActionBar.TabListener, 
		RubyChinaActor, OnTopicSelectedListener, OnPageChangeListener {

	public static final String VIEW_PROFILE = "org.rubychina.android.activity.UserProfileActivity.VIEW_PROFILE";
	
	private static final String TAG = "UserIndexActivity";
	
	private RCService mService;
	private boolean isBound = false; 
	private DisplayMetrics metrics;
	
	private ViewPager  mViewPager;
	private ProfilePagerAdapter mAdapter;
	
	private User user;
	
	private final int TAB_PROFILE = 0;
	private final int TAB_TOPIC_CREATED = 1;
	private final int TAB_TOPIC_FAVORITE = 2;
	
	private ArrayList<Fragment> mPages = new ArrayList<Fragment>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.user_index_tabs_pager_layout);
        metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		user = getIntent().getExtras().getParcelable(VIEW_PROFILE);
        setTitle(user.getLogin());
        Intent intent = new Intent(this, RCService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(TAG, "on new intent");
	}

	private ServiceConnection mConnection = new ServiceConnection() {
	
	    @Override
	    public void onServiceConnected(ComponentName className,
	            IBinder service) {
	        LocalBinder binder = (LocalBinder) service;
	        mService = binder.getService();
	        isBound = true;
	        initialize();
	    }
	
		@Override
	    public void onServiceDisconnected(ComponentName arg0) {
	        isBound = false;
	    }
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isBound) {
			unbindService(mConnection);
			isBound = false;
		}
	}

    private void initialize() {
    	mViewPager = (ViewPager) findViewById(R.id.pager);
    	mAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
    	mViewPager.setAdapter(mAdapter);
    	mViewPager.setCurrentItem(TAB_PROFILE);
    	mViewPager.setOnPageChangeListener(this);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getSupportActionBar().addTab(getProfileTab());
        getSupportActionBar().addTab(getRecentlyCreatedTopicTab());
        getSupportActionBar().addTab(getFavoriteTopicTab());
        getSupportActionBar().setSelectedNavigationItem(TAB_PROFILE);
	}
	
	private ActionBar.Tab getProfileTab() {
		ActionBar.Tab tab = getSupportActionBar().newTab();
        tab.setText(R.string.tab_profile);
        tab.setTag(getString(R.string.tab_profile));
        tab.setTabListener(this);
        return tab;
	}
	
	private ActionBar.Tab getRecentlyCreatedTopicTab() {
		ActionBar.Tab tab = getSupportActionBar().newTab();
        tab.setText(R.string.tab_topic);
        tab.setTag(getString(R.string.tab_topic));
        tab.setTabListener(this);
        return tab;
	}
	
	private ActionBar.Tab getFavoriteTopicTab() {
		ActionBar.Tab tab = getSupportActionBar().newTab();
		tab.setText(R.string.tab_favorite);
		tab.setTag(getString(R.string.tab_favorite));
		tab.setTabListener(this);
		return tab;
	}
	
    @Override
	public RCApplication getApp() {
		return (RCApplication) getApplication();
	}
	
	@Override
	public RCService getService() {
		return mService;
	}
	
	@Override
	public RCAPIClient getClient() {
		return ((RCApplication) getApplication()).getAPIClient();
	}

	@Override
	public void showIndeterminateProgressBar() {
		setSupportProgressBarIndeterminateVisibility(true);
	}

	@Override
	public void hideIndeterminateProgressBar() {
		setSupportProgressBarIndeterminateVisibility(false);
	}

	@Override
	public void onCompose() {
		Intent i = new Intent();
		if(((RCApplication) getApplication()).isLogin()) {
    		i.setClass(getApplicationContext(), TopicEditingActivity.class);
    	} else {
    		i.setClass(getApplicationContext(), RCPreferenceActivity.class);
    		Toast.makeText(getApplicationContext(), R.string.hint_no_token, Toast.LENGTH_SHORT).show();
    	}
    	startActivity(i);
	}
	
	@Override
	public void onSetting() {
		Intent i = new Intent();
		i.setClass(getApplicationContext(), RCPreferenceActivity.class);
		startActivity(i);
	}
	
	@Override
	public void onTopicSelected(List<Topic> topics, int position) {
		Intent i = new Intent(this, TopicDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(TopicDetailActivity.POS, position);
		bundle.putParcelableArrayList(TopicDetailActivity.TOPICS,
				(ArrayList<? extends Parcelable>) topics);
		i.putExtras(bundle);
		startActivity(i);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		switch(tab.getPosition()) {
		case TAB_PROFILE:
			mViewPager.setCurrentItem(TAB_PROFILE, true);
			break;
		case TAB_TOPIC_CREATED:
			mViewPager.setCurrentItem(TAB_TOPIC_CREATED, true);
			break;
		case TAB_TOPIC_FAVORITE:
			mViewPager.setCurrentItem(TAB_TOPIC_FAVORITE, true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}
    
	public class ProfilePagerAdapter extends FragmentPagerAdapter {

		public ProfilePagerAdapter(FragmentManager fm) {
			super(fm);
			mPages.add(UserProfileFragment.newInstance(user, metrics.density));
			mPages.add(UserRecentlyCreatedTopicListFragment.newInstance(user));
			mPages.add(UserFavoriteTopicListFragment.newInstance(user));
		}

		@Override
		public Fragment getItem(int position) {
			return mPages.get(position);
		}

		@Override
		public int getCount() {
			return mPages.size();
		}

    }

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		getSupportActionBar().setSelectedNavigationItem(position);
	}
	
}
