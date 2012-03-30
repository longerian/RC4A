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

import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.LoaderActionBarItem;

import java.util.List;

import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.RCService;
import org.rubychina.android.RCService.LocalBinder;
import org.rubychina.android.api.request.TopicsRequest;
import org.rubychina.android.api.response.TopicsResponse;
import org.rubychina.android.type.Node;
import org.rubychina.android.type.Topic;
import org.rubychina.android.type.User;
import org.rubychina.android.util.JsonUtil;
import org.rubychina.android.widget.TopicAdapter;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


public class TopicsActivity extends GDListActivity {

	private static final String TAG = "TopicsActivity";
	private TopicsRequest request;
	
	
	private RCService mService;
	private boolean isBound = false; 
	
	private LoaderActionBarItem progress;
	
	private Node node = Node.MOCK_ACTIVE_NODE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addActionBarItem(Type.List, R.id.action_bar_nodes);
		progress = (LoaderActionBarItem) addActionBarItem(Type.Refresh, R.id.action_bar_refresh);
		addActionBarItem(Type.Compose, R.id.action_bar_compose);
		
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
		cancelTopicsRequest();
	}
	
	private void initialize() {
		List<Topic> cachedTopics = mService.fetchTopics();
		refreshPage(cachedTopics);
		startTopicsRequest(node);
	}
	
	private void refreshPage(List<Topic> topics) {
		TopicAdapter adapter = new TopicAdapter(this, 
				R.layout.topic_item,
				R.id.title, 
				topics);
		setListAdapter(adapter);
	}
	
	private void startTopicsRequest(Node node) {
		if(request == null) {
			request = new TopicsRequest();
		}
		request.setNodeId(node.getId());
		request.setSize(((RCApplication) getApplication()).getPageSize());
		((RCApplication) getApplication()).getAPIClient().request(request, new ActiveTopicsCallback());
		progress.setLoading(true);
	}
	
	private void cancelTopicsRequest() {
		if(request != null) {
			((RCApplication) getApplication()).getAPIClient().cancel(request);
			progress.setLoading(false);
		}
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		Intent i = new Intent();
		switch (item.getItemId()) {
		case R.id.action_bar_nodes:
			i.setClass(getApplicationContext(), NodesActivity.class);
			startActivityForResult(i, NodesActivity.PICK_NODE);
			return true;
        case R.id.action_bar_refresh:
        	startTopicsRequest(node);
        	return true;
        case R.id.action_bar_compose:
        	if(((RCApplication) getApplication()).isLogin()) {
        		i.setClass(getApplicationContext(), TopicEditingActivity.class);
        	} else {
        		i.setClass(getApplicationContext(), RCPreferenceActivity.class);
        		Toast.makeText(getApplicationContext(), R.string.hint_no_token, Toast.LENGTH_SHORT).show();
        	}
        	startActivity(i);
        	return true;
        default:
            return super.onHandleActionBarItemClick(item, position);
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(getApplicationContext(), TopicDetailActivity.class);
		Topic t = (Topic) l.getItemAtPosition(position);
		i.putExtra(TopicDetailActivity.TOPIC, JsonUtil.toJsonObject(t));
		i.putExtra(TopicDetailActivity.POS, position);
		startActivity(i);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == NodesActivity.PICK_NODE) {
			if(resultCode == RESULT_OK) {
				Node n = data.getParcelableExtra(NodesActivity.PICKED_NODE);
				node = n;
				startTopicsRequest(n);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(getApplication()).inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		int id = item.getItemId();
		switch(id) {
		case R.id.menu_preference:
			intent.setClass(getApplicationContext(), RCPreferenceActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}

	private class ActiveTopicsCallback implements ApiCallback<TopicsResponse> {

		@Override
		public void onException(ApiException e) {
			Toast.makeText(getApplicationContext(), R.string.hint_network_error, Toast.LENGTH_SHORT).show();
			progress.setLoading(false);
		}

		@Override
		public void onFail(TopicsResponse r) {
			Toast.makeText(getApplicationContext(), R.string.hint_loading_data_failed, Toast.LENGTH_SHORT).show();
			progress.setLoading(false);
		}

		@Override
		public void onSuccess(TopicsResponse r) {
			progress.setLoading(false);
			refreshPage(r.getTopics());
			new CacheTopicsTask().execute(r.getTopics());
		}
		
	}
	
	private class CacheTopicsTask extends AsyncTask<List<Topic>, Void, Void> {

		@Override
		protected void onPreExecute() {
			progress.setLoading(true);
		}

		@Override
		protected Void doInBackground(List<Topic>... params) {
			mService.clearTopics();
			mService.insertTopics(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			progress.setLoading(false);
		}
		
	}

	public void visitUserProfile(User u) {
		Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
		i.putExtra(UserProfileActivity.VIEW_PROFILE, JsonUtil.toJsonObject(u));
		startActivity(i);
	}
	
	public void requestUserAvatar(User u, ImageView v, int size) {
		if(isBound) {
			mService.requestUserAvatar(u, v, size);
		}
	}
	
}
