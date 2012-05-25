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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.RCService;
import org.rubychina.android.RCService.LocalBinder;
import org.rubychina.android.api.request.NodesRequest;
import org.rubychina.android.api.response.NodesResponse;
import org.rubychina.android.type.Node;
import org.rubychina.android.type.Section;
import org.rubychina.android.widget.NodeAdapter;
import org.rubychina.android.widget.SeparatedListAdapter;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class NodesActivity<V> extends SherlockFragmentActivity {

	private static final String TAG = "NodesActivity";
	public static final int PICK_NODE = 0x7001;
	public static final String PICKED_NODE = "org.rubychina.android.activity.NodesActivity.PICKED_NODE";
	
	private NodesRequest request;
	private RCService mService;
	private boolean isBound = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setTitle(R.string.title_nodes);
		if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
			NodeListFragment nodesList = new NodeListFragment();
	        getSupportFragmentManager().beginTransaction().add(android.R.id.content, nodesList).commit();
		}
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
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, R.id.action_bar_refresh, 0, R.string.actionbar_refresh)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
        case R.id.action_bar_refresh:
        	startNodesRequest();
        	break;
		default: 
			break;
		}
		return true;
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
		setSupportProgressBarIndeterminateVisibility(true);
	}
	
	private void cancelNodesRequest() {
		if(request != null) {
			((RCApplication) getApplication()).getAPIClient().cancel(request);
		}
		setSupportProgressBarIndeterminateVisibility(false);
	}
	
	private void refreshPage(List<Node> nodes) {
		if(!nodes.contains(Node.MOCK_ACTIVE_NODE)) {
			nodes.add(0, Node.MOCK_ACTIVE_NODE);
		}
		Map<Section, List<Node>> groupedNodes = new TreeMap<Section, List<Node>>();
		for(Node n : nodes) {
			Section s = n.whichSection();
			if(groupedNodes.containsKey(s)) {
				groupedNodes.get(s).add(n);
			} else {
				List<Node> ns = new ArrayList<Node>();
				ns.add(n);
				groupedNodes.put(s, ns);
			}
		}
		SeparatedListAdapter adapter = new SeparatedListAdapter(getApplicationContext());
		Iterator<Entry<Section, List<Node>>> iter = groupedNodes.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<Section, List<Node>> entry = (Entry<Section, List<Node>>) iter.next();
			adapter.addSection(entry.getKey(), 
					new NodeAdapter(getApplicationContext(), R.layout.node_item, entry.getValue()));
		}
		NodeListFragment list = (NodeListFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
		list.setListAdapter(adapter);
	}
	
	private class NodesCallback implements ApiCallback<NodesResponse> {

		@Override
		public void onException(ApiException e) {
			setSupportProgressBarIndeterminateVisibility(false);
			Toast.makeText(getApplicationContext(), R.string.hint_network_error, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onFail(NodesResponse r) {
			setSupportProgressBarIndeterminateVisibility(false);
			Toast.makeText(getApplicationContext(), R.string.hint_loading_data_failed, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onSuccess(NodesResponse r) {
			setSupportProgressBarIndeterminateVisibility(false);
			refreshPage(r.getNodes());
			new CacheNodesTask().execute(r.getNodes());
		}
		
	}
	
	private class CacheNodesTask extends AsyncTask<List<Node>, Void, Void> {

		@Override
		protected void onPreExecute() {
			setSupportProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Void doInBackground(List<Node>... params) {
			mService.clearNodes();
			mService.insertNodes(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			setSupportProgressBarIndeterminateVisibility(false);
		}
		
	}
	
	private class NodeListFragment extends SherlockListFragment {

        @Override
		public void onStart() {
			super.onStart();
			Intent intent = new Intent(getActivity(), RCService.class);
	        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		}

		@Override
		public void onStop() {
			super.onStop();
			if (isBound) {
	            unbindService(mConnection);
	            isBound = false;
	        }
		}

        @Override
		public void onDestroy() {
			super.onDestroy();
			cancelNodesRequest();
		}

		@Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Node n = (Node) l.getItemAtPosition(position);
    		Intent i = new Intent();
    		i.putExtra(PICKED_NODE, n);
    		setResult(RESULT_OK, i);
    		finish();
        }
    }
	
}
