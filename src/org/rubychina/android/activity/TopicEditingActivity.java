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
import org.rubychina.android.api.request.NodesRequest;
import org.rubychina.android.api.request.PostTopicRequest;
import org.rubychina.android.api.response.NodesResponse;
import org.rubychina.android.api.response.PostTopicResponse;
import org.rubychina.android.fragment.RCAlertDialogFragment;
import org.rubychina.android.fragment.RCAlertDialogFragment.OnRCDialogFragmentClickListener;
import org.rubychina.android.type.Node;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class TopicEditingActivity extends SherlockFragmentActivity implements OnRCDialogFragmentClickListener {

	private EditText title;
	private Spinner nodeSelector;
	private EditText body;
	private ProgressDialog sending;
	private PostTopicRequest request;
	private NodesRequest nodeRequest;
	private RCService mService;
	private boolean isBound = false; 
	
	private RCAlertDialogFragment dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.title_posting_new_topic);
		setContentView(R.layout.topic_editing_layout);
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, R.id.action_bar_compose, 0, R.string.actionbar_compose)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
        case R.id.action_bar_compose:
        	if(isTopicValid()) {
        		startPostTopicRequest(
        				title.getText().toString(),
        				((Node) nodeSelector.getSelectedItem()).getId() + "",
        				body.getText().toString()
        				);
        	}
        	break;
		default: 
			break;
		}
		return true;
	}
	
	private void initialize() {
		title = (EditText) findViewById(R.id.title);
		nodeSelector = (Spinner) findViewById(R.id.node);
		List<Node> nodes = mService.fetchNodes();
		if(nodes.contains(Node.MOCK_ACTIVE_NODE)) {
			nodes.remove(Node.MOCK_ACTIVE_NODE);
		}
		ArrayAdapter<Node> adapter = new ArrayAdapter<Node>(getApplicationContext(), 
				android.R.layout.simple_spinner_item, 
				nodes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		nodeSelector.setAdapter(adapter);
		body = (EditText) findViewById(R.id.body);
		
		if(nodes.isEmpty()) {
			startNodesRequest();
		}
	}

	private boolean isTopicValid() {
		if(title == null || body == null) {
			return false;
		}
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
		if(TextUtils.isEmpty(title.getText().toString())) {
			title.startAnimation(animation);
			return false;
		}
		if(TextUtils.isEmpty(body.getText().toString())) {
			body.startAnimation(animation);
			return false;
		}
		if((Node) nodeSelector.getSelectedItem() == null) {
			nodeSelector.startAnimation(animation);
			return false;
		}
		return true;
	}
	
	private void startPostTopicRequest(String title, String nodeId, String body) {
		if(request == null) {
			request = new PostTopicRequest();
		}
		request.setTitle(title);
		request.setNodeId(nodeId);
		request.setBody(body);
		((RCApplication) getApplication()).getAPIClient().request(request, new PostTopicCallback());
		showProgress(R.string.hint_submiting_topic);
	}
	
	private void showProgress(int  message) {
		if(sending == null) {
			sending = new ProgressDialog(this);
			sending.setCancelable(false);
		}
		sending.setMessage(getString(message));
		sending.show();
	}
	
	private void dismissProgress() {
		if(sending != null) {
			sending.dismiss();
		}
	}
	
	private void startNodesRequest() {
		if(nodeRequest == null) {
			nodeRequest = new NodesRequest();
		}
		((RCApplication) getApplication()).getAPIClient().request(nodeRequest, new NodesCallback());
		showProgress(R.string.hint_loading_nodes);
	}
	
	private class NodesCallback implements ApiCallback<NodesResponse> {

		@Override
		public void onException(ApiException e) {
			dismissProgress();
			Toast.makeText(getApplicationContext(), 
					R.string.hint_no_nodes, 
					Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onFail(NodesResponse r) {
			dismissProgress();
			Toast.makeText(getApplicationContext(), 
					R.string.hint_no_nodes,
					Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onSuccess(NodesResponse r) {
			dismissProgress();
			ArrayAdapter<Node> adapter = new ArrayAdapter<Node>(getApplicationContext(), 
					android.R.layout.simple_spinner_item, 
					r.getNodes());
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			nodeSelector.setAdapter(adapter);
			new CacheNodesTask().execute(r.getNodes());
		}
		
	}
	
	private class CacheNodesTask extends AsyncTask<List<Node>, Void, Void> {

		@Override
		protected Void doInBackground(List<Node>... params) {
			mService.clearNodes();
			mService.insertNodes(params[0]);
			return null;
		}
		
	}
	
	private class PostTopicCallback implements ApiCallback<PostTopicResponse> {

		@Override
		public void onException(ApiException e) {
			dismissProgress();
			Toast.makeText(getApplicationContext(), 
					R.string.hint_network_or_token_error, 
					Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		}

		@Override
		public void onFail(PostTopicResponse r) {
			dismissProgress();
			Toast.makeText(getApplicationContext(), 
					R.string.hint_network_or_token_error, 
					Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onSuccess(PostTopicResponse r) {
			dismissProgress();
			Intent homeIntent = new Intent(TopicEditingActivity.this, TopicsActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(!TextUtils.isEmpty(title.getText().toString())
					|| !TextUtils.isEmpty(body.getText().toString())) {
				dialog = RCAlertDialogFragment.newInstance(R.string.hint_stoping_editing_topic_or_not);
				dialog.show(getSupportFragmentManager(), "dialog");
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void doPositiveClick() {
		finish();
	}

	@Override
	public void doNegativeClick() {
		dialog.dismiss();
	}
	
}
