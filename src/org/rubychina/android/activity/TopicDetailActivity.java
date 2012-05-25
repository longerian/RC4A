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

import java.util.List;

import org.rubychina.android.R;
import org.rubychina.android.RCService;
import org.rubychina.android.RCService.LocalBinder;
import org.rubychina.android.type.Topic;
import org.rubychina.android.type.User;
import org.rubychina.android.widget.TopicPagerAdapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.text.Html.ImageGetter;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TopicDetailActivity extends SherlockFragmentActivity {

	public static final String POS = "org.rubychina.android.activity.TopicDetailActivity.POSITION";
	public static final String TOPICS = "org.rubychina.android.activity.TopicDetailActivity.TOPICS";
	private static final String TAG = "TopicDetailActivity";
	
	private RCService mService;
	private boolean isBound = false; 
	
	private List<Topic> topics;
	private int currentPosition;
	 
	private TopicPagerAdapter pagerAdapter;
	private ViewPager mPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_pager_layout);
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
    	topics = getIntent().getExtras().getParcelableArrayList(TOPICS);
		currentPosition = getIntent().getExtras().getInt(POS);
    	mPager = (ViewPager) findViewById(R.id.topic_pager);
		pagerAdapter = new TopicPagerAdapter(getSupportFragmentManager(), topics);
		mPager.setAdapter(pagerAdapter);
		mPager.setCurrentItem(currentPosition);
    }
    
	public void requestUserAvatar(User u, ImageView v, int size) {
		if(isBound) {
			mService.requestUserAvatar(u, v, size);
		}
	}
	
	public ImageGetter getImageGetter() {
		return mService.getImageGetter();
	}
}
