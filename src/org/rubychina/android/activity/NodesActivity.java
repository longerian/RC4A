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
import org.rubychina.android.api.request.NodesRequest;
import org.rubychina.android.api.response.NodesResponse;
import org.rubychina.android.type.Node;

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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NodesActivity extends GDListActivity {

	public static final int PICK_NODE = 0x7001;
	public static final String PICKED_NODE = "org.rubychina.android.activity.NodesActivity.PICKED_NODE";
	private static final String TAG = "NodesActivity";
	
	public static final String ACTIVE_TOPICS_NODE_NAME = "热门话题";
	public static final int ACTIVE_TOPICS_NODE_ID = -1;
	public static final Node ACTIVE_TOPICS_NODE = new Node(ACTIVE_TOPICS_NODE_ID, ACTIVE_TOPICS_NODE_NAME);
	
	private NodesRequest request;
	private LoaderActionBarItem progress;
	private RCService mService;
	private boolean isBound = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progress = (LoaderActionBarItem) addActionBarItem(Type.Refresh, R.id.action_bar_refresh);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
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
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		cancelNodesRequest();
	}
	
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
        case R.id.action_bar_refresh:
        	startNodesRequest();
        	return true;
        default:
            return super.onHandleActionBarItemClick(item, position);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Node n = (Node) l.getItemAtPosition(position);
		Intent i = new Intent();
		i.putExtra(PICKED_NODE, n);
		setResult(RESULT_OK, i);
		finish();
	}

	private void initialize() {
		List<Node> nodes = mService.fetchNodes();
		if(nodes.isEmpty()) {
			startNodesRequest();
		} else {
			refreshPage(nodes);
		}
	}
	
	private void startNodesRequest() {
		if(request == null) {
			request = new NodesRequest();
		}
		((RCApplication) getApplication()).getAPIClient().request(request, new NodesCallback());
		progress.setLoading(true);
	}
	
	private void cancelNodesRequest() {
		if(request != null) {
			((RCApplication) getApplication()).getAPIClient().cancel(request);
			progress.setLoading(false);
		}
	}
	
	private void refreshPage(List<Node> nodes) {
		if(!nodes.contains(ACTIVE_TOPICS_NODE)) {
			nodes.add(0, ACTIVE_TOPICS_NODE);
		}
		NodeAdapter adapter = new NodeAdapter(getApplicationContext(), R.layout.node_item,
				R.id.name, nodes);
		setListAdapter(adapter);
	}
	
	private class NodesCallback implements ApiCallback<NodesResponse> {

		@Override
		public void onException(ApiException e) {
			Toast.makeText(getApplicationContext(), R.string.hint_loading_data_failed, Toast.LENGTH_SHORT).show();
			progress.setLoading(false);
		}

		@Override
		public void onFail(NodesResponse r) {
			Toast.makeText(getApplicationContext(), R.string.hint_loading_data_failed, Toast.LENGTH_SHORT).show();
			progress.setLoading(false);
		}

		@Override
		public void onSuccess(NodesResponse r) {
			progress.setLoading(false);
			refreshPage(r.getNodes());
			new CacheNodesTask().execute(r.getNodes());
		}
		
	}
	
	private class CacheNodesTask extends AsyncTask<List<Node>, Void, Void> {

		@Override
		protected void onPreExecute() {
			progress.setLoading(true);
		}

		@Override
		protected Void doInBackground(List<Node>... params) {
			mService.clearNodes();
			mService.insertNodes(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			progress.setLoading(false);
		}
		
	}
	
	private class NodeAdapter extends ArrayAdapter<Node> {

		private List<Node> items;
		private Context context;
		private int resource;
		
		public NodeAdapter(Context context, int resource,
				int textViewResourceId, List<Node> items) {
			super(context, resource, textViewResourceId, items);
			this.context = context;
			this.resource = resource;
			this.items = items;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if(convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(resource, null);
				viewHolder.name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Node n = items.get(position);
			viewHolder.name.setText(n.getName());
			return convertView;
		}
		
		private class ViewHolder {
			
			public TextView name;
			
		}
		
	}
	
}
