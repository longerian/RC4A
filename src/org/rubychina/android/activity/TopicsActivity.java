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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


public class TopicsActivity extends SherlockFragmentActivity {

	private static final String TAG = "TopicsActivity";
	private TopicsRequest request;
	
	
	private RCService mService;
	private boolean isBound = false; 
	
	private ListView topicsView;
	private TextView nodeSection;
	
	private Node node = Node.MOCK_ACTIVE_NODE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topics_layout);
		
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
		refreshPage(cachedTopics, node);//TODO node info must also be persisted
		startTopicsRequest(node);
	}
	
	private void initializeView(Node node) {
		if(topicsView == null) {
			topicsView = (ListView) findViewById(R.id.topics);
			if(nodeSection == null) {
				nodeSection = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.node_section_header, null);
			}
			topicsView.addHeaderView(nodeSection, null, false);
			topicsView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
						Intent i = new Intent(getApplicationContext(), TopicDetailActivity.class);
						Topic t = (Topic) parent.getItemAtPosition(position);
						i.putExtra(TopicDetailActivity.TOPIC, JsonUtil.toJsonObject(t));
						i.putExtra(TopicDetailActivity.POS, position);
						startActivity(i);
				}
			});
		}
		nodeSection.setText(node.getName());
	}
	
	private void refreshPage(List<Topic> topics, Node node) {
		initializeView(node);
		TopicAdapter adapter = new TopicAdapter(this, 
				R.layout.topic_item,
				R.id.title, 
				topics);
		topicsView.setAdapter(adapter);
	}
	
	private void startTopicsRequest(Node node) {
		if(request == null) {
			request = new TopicsRequest();
		}
		request.setNodeId(node.getId());
		request.setSize(((RCApplication) getApplication()).getPageSize());
		((RCApplication) getApplication()).getAPIClient().request(request, new ActiveTopicsCallback());
//		progress.setLoading(true);
	}
	
	private void cancelTopicsRequest() {
		if(request != null) {
			((RCApplication) getApplication()).getAPIClient().cancel(request);
//			progress.setLoading(false);
		}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, R.id.action_bar_nodes, 0, R.string.actionbar_nodes)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(0, R.id.action_bar_compose, 1, R.string.actionbar_compose)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(0, R.id.action_bar_refresh, 2, R.string.actionbar_refresh)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(0, R.id.action_bar_setting, 2, R.string.actionbar_setting)
        	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = new Intent();
		switch(item.getItemId()) {
		case R.id.action_bar_nodes:
			i.setClass(getApplicationContext(), NodesActivity.class);
			startActivityForResult(i, NodesActivity.PICK_NODE);
			break;
        case R.id.action_bar_refresh:
        	startTopicsRequest(node);
        	break;
        case R.id.action_bar_compose:
        	onCompose();
        	break;
        case R.id.action_bar_setting:
			i.setClass(getApplicationContext(), RCPreferenceActivity.class);
			startActivity(i);
			break;
		default: 
			break;
		}
		return true;
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

	private void onCompose() {
		Intent i = new Intent();
		if(((RCApplication) getApplication()).isLogin()) {
    		i.setClass(getApplicationContext(), TopicEditingActivity.class);
    	} else {
    		i.setClass(getApplicationContext(), RCPreferenceActivity.class);
    		Toast.makeText(getApplicationContext(), R.string.hint_no_token, Toast.LENGTH_SHORT).show();
    	}
    	startActivity(i);
	}
	
	private class ActiveTopicsCallback implements ApiCallback<TopicsResponse> {

		@Override
		public void onException(ApiException e) {
			Toast.makeText(getApplicationContext(), R.string.hint_network_error, Toast.LENGTH_SHORT).show();
//			progress.setLoading(false);
		}

		@Override
		public void onFail(TopicsResponse r) {
			Toast.makeText(getApplicationContext(), R.string.hint_loading_data_failed, Toast.LENGTH_SHORT).show();
//			progress.setLoading(false);
		}

		@Override
		public void onSuccess(TopicsResponse r) {
//			progress.setLoading(false);
			refreshPage(r.getTopics(), node);
			new CacheTopicsTask().execute(r.getTopics());
		}
		
	}
	
	private class CacheTopicsTask extends AsyncTask<List<Topic>, Void, Void> {

		@Override
		protected void onPreExecute() {
//			progress.setLoading(true);
		}

		@Override
		protected Void doInBackground(List<Topic>... params) {
			mService.clearTopics();
			mService.insertTopics(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
//			progress.setLoading(false);
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
