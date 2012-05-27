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
import org.rubychina.android.fragment.ContentDialogFragment;
import org.rubychina.android.fragment.NodeListFragment;
import org.rubychina.android.fragment.NodeListFragment.OnNodeSelectedListener;
import org.rubychina.android.fragment.SiteListFragment;
import org.rubychina.android.fragment.SiteListFragment.OnSiteSelectedListener;
import org.rubychina.android.fragment.TopicListFragment;
import org.rubychina.android.fragment.TopicListFragment.OnTopicSelectedListener;
import org.rubychina.android.fragment.UserListFragment;
import org.rubychina.android.fragment.UserListFragment.OnUserSelectedListener;
import org.rubychina.android.type.Node;
import org.rubychina.android.type.Site;
import org.rubychina.android.type.Topic;
import org.rubychina.android.type.User;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;


public class RubyChinaIndexActivity extends SherlockFragmentActivity implements OnTopicSelectedListener, OnUserSelectedListener,
	OnNodeSelectedListener,OnSiteSelectedListener, ActionBar.TabListener, RubyChinaActor {

	private static final String TAG = "RubyChinaIndexActivity";
	
	private RCService mService;
	private boolean isBound = false; 
	
	private final int TAB_TOPIC = 0;
	private final int TAB_NODE = 1;
	private final int TAB_SITE = 2;
	private final int TAB_USER = 3;
	
	private Fragment lastFragment;
	private TopicListFragment topicListFragment;
	private NodeListFragment nodeListFragment;
	private SiteListFragment siteListFragment;
	private UserListFragment userListFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getSupportActionBar().addTab(getTopicTab());
        getSupportActionBar().addTab(getNodeTab());
        getSupportActionBar().addTab(getSiteTab());
        getSupportActionBar().addTab(getUserTab());
        getSupportActionBar().setSelectedNavigationItem(TAB_TOPIC);
	}
	
	private ActionBar.Tab getTopicTab() {
		ActionBar.Tab tab = getSupportActionBar().newTab();
        tab.setText(R.string.tab_topic);
        tab.setTag(getString(R.string.tab_topic));
        tab.setTabListener(this);
        return tab;
	}
	
	private ActionBar.Tab getNodeTab() {
		ActionBar.Tab tab = getSupportActionBar().newTab();
        tab.setText(R.string.tab_node);
        tab.setTag(getString(R.string.tab_node));
        tab.setTabListener(this);
        return tab;
	}
	
	private ActionBar.Tab getSiteTab() {
		ActionBar.Tab tab = getSupportActionBar().newTab();
		tab.setText(R.string.tab_site);
		tab.setTag(getString(R.string.tab_site));
		tab.setTabListener(this);
		return tab;
	}
	
	private ActionBar.Tab getUserTab() {
		ActionBar.Tab tab = getSupportActionBar().newTab();
		tab.setText(R.string.tab_user);
		tab.setTag(getString(R.string.tab_user));
		tab.setTabListener(this);
		return tab;
	}
	
	private void displayContent(Fragment fragment, String tag) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if(lastFragment != null) {
			ft.hide(lastFragment);
		}
		Fragment existedFragment = fm.findFragmentByTag(tag);
		if(existedFragment == null) {
	        ft.add(android.R.id.content, fragment, tag);
		} else {
			if(existedFragment != fragment) {
				ft.remove(existedFragment);
				ft.add(android.R.id.content, fragment, tag);
			}
		}
		ft.show(fragment);
		lastFragment = fragment;
		ft.commit();
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
	public void onUserSelected(User user) {
		Intent i = new Intent(this, UserIndexActivity.class);
		Bundle b = new Bundle();
		b.putParcelable(UserIndexActivity.VIEW_PROFILE, user);
		i.putExtras(b);
		startActivity(i);
	}
	
	@Override
	public void onNodeSelected(Node node) {
		topicListFragment = TopicListFragment.newInstance(node);
		getSupportActionBar().setSelectedNavigationItem(TAB_TOPIC);
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
	public void onSiteSelected(Site site) {
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(site.getUrl()));
		startActivity(i);
	}

	@Override
	public void onSiteDescSelected(Site site) {
		ContentDialogFragment cdf = ContentDialogFragment.newInstance(getString(R.string.title_site_desc), site.getDesc());
		cdf.show(getSupportFragmentManager(), null);
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		switch(tab.getPosition()) {
		case TAB_TOPIC:
			if(topicListFragment == null) {
				topicListFragment = TopicListFragment.newInstance(Node.MOCK_ACTIVE_NODE);
			}
			displayContent(topicListFragment, (String) tab.getTag());
			break;
		case TAB_NODE:
			if(nodeListFragment == null) {
				nodeListFragment = new NodeListFragment();
			}
			displayContent(nodeListFragment, (String) tab.getTag());
			break;
		case TAB_SITE:
			if(siteListFragment == null) {
				siteListFragment = new SiteListFragment();
			}
			displayContent(siteListFragment, (String) tab.getTag());
			break;
		case TAB_USER:
			if(userListFragment == null) {
				userListFragment = new UserListFragment();
			}
			displayContent(userListFragment, (String) tab.getTag());
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

}
