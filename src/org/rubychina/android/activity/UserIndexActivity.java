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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

public class UserIndexActivity extends SherlockFragmentActivity implements RubyChinaActor, OnTopicSelectedListener {

	public static final String VIEW_PROFILE = "org.rubychina.android.activity.UserProfileActivity.VIEW_PROFILE";
	
	private static final String TAG = "UserIndexActivity";
	
	private RCService mService;
	private boolean isBound = false; 
	private DisplayMetrics metrics;
	
	private Bundle savedInstanceState;
	
	private TabHost mTabHost;
	private ViewPager  mViewPager;
	private TabsAdapter mTabsAdapter;
	
	private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setContentView(R.layout.user_index_tabs_pager_layout);
        this.savedInstanceState = savedInstanceState;
//        if(getIntent().getStringExtra(VIEW_PROFILE) != null) {
//			user = JsonUtil.fromJsonObject(getIntent().getStringExtra(VIEW_PROFILE), User.class);
//		} else {
			user = getIntent().getExtras().getParcelable(VIEW_PROFILE);
//		}
        setTitle(user.getLogin());
        Intent intent = new Intent(this, RCService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		hideIndeterminateProgressBar();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }
    
    private void initialize() {
    	mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        Bundle bundle = new Bundle();
        bundle.putParcelable(UserProfileFragment.USER, user);
        bundle.putFloat(UserProfileFragment.DENSITY, metrics.density);
        mTabsAdapter.addTab(mTabHost.newTabSpec(getString(R.string.tab_profile)).setIndicator(getString(R.string.tab_profile)),
                UserProfileFragment.class, bundle);
        mTabsAdapter.addTab(mTabHost.newTabSpec(getString(R.string.tab_topic)).setIndicator(getString(R.string.tab_topic)),
                UserRecentlyCreatedTopicListFragment.class, bundle);
        mTabsAdapter.addTab(mTabHost.newTabSpec(getString(R.string.tab_favorite)).setIndicator(getString(R.string.tab_favorite)),
                UserFavoriteTopicListFragment.class, bundle);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
	}
    
    public static class TabsAdapter extends FragmentPagerAdapter
		    implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final TabHost mTabHost;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
		
		static final class TabInfo {
		    private final String tag;
		    private final Class<?> clss;
		    private final Bundle args;
		
		    TabInfo(String _tag, Class<?> _class, Bundle _args) {
		        tag = _tag;
		        clss = _class;
		        args = _args;
		    }
		}
		
		static class DummyTabFactory implements TabHost.TabContentFactory {
		    private final Context mContext;
		
		    public DummyTabFactory(Context context) {
		        mContext = context;
		    }
		
		    @Override
		    public View createTabContent(String tag) {
		        View v = new View(mContext);
		        v.setMinimumWidth(0);
		        v.setMinimumHeight(0);
		        return v;
		    }
		}
		
		public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
		    super(activity.getSupportFragmentManager());
		    mContext = activity;
		    mTabHost = tabHost;
		    mViewPager = pager;
		    mTabHost.setOnTabChangedListener(this);
		    mViewPager.setAdapter(this);
		    mViewPager.setOnPageChangeListener(this);
		}
		
		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
		    tabSpec.setContent(new DummyTabFactory(mContext));
		    String tag = tabSpec.getTag();
		
		    TabInfo info = new TabInfo(tag, clss, args);
		    mTabs.add(info);
		    mTabHost.addTab(tabSpec);
		    notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
		    return mTabs.size();
		}
		
		@Override
		public Fragment getItem(int position) {
		    TabInfo info = mTabs.get(position);
		    return Fragment.instantiate(mContext, info.clss.getName(), info.args);
		}
		
		@Override
		public void onTabChanged(String tabId) {
		    int position = mTabHost.getCurrentTab();
		    mViewPager.setCurrentItem(position);
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}
		
		@Override
		public void onPageSelected(int position) {
		    // Unfortunately when TabHost changes the current tab, it kindly
		    // also takes care of putting focus on it when not in touch mode.
		    // The jerk.
		    // This hack tries to prevent this from pulling focus out of our
		    // ViewPager.
		    TabWidget widget = mTabHost.getTabWidget();
		    int oldFocusability = widget.getDescendantFocusability();
		    widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		    mTabHost.setCurrentTab(position);
		    widget.setDescendantFocusability(oldFocusability);
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
		}
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
		setSupportProgressBarIndeterminate(true);
	}

	@Override
	public void hideIndeterminateProgressBar() {
		setSupportProgressBarIndeterminate(false);
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
    
}
