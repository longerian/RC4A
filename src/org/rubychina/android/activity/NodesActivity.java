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
import org.rubychina.android.fragment.NodeListFragment;
import org.rubychina.android.fragment.NodeListFragment.OnNodeSelectedListener;
import org.rubychina.android.type.Node;
import org.rubychina.android.type.Topic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class NodesActivity extends SherlockFragmentActivity implements OnNodeSelectedListener {

	private static final String TAG = "NodesActivity";
	public static final int PICK_NODE = 0x7001;
	public static final String PICKED_NODE = "org.rubychina.android.activity.NodesActivity.PICKED_NODE";
	
	private RCService mService;
	private boolean isBound = false;
	
	private NodeListFragment nodesList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setTitle(R.string.title_nodes);
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
        menu.add(0, R.id.action_bar_refresh, 0, R.string.actionbar_refresh)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
        case R.id.action_bar_refresh:
        	nodesList.startNodesRequest();
        	break;
		default: 
			break;
		}
		return true;
	}
    
	private void initialize() {
		if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
			nodesList = new NodeListFragment();
	        getSupportFragmentManager().beginTransaction().add(android.R.id.content, nodesList).commit();
		}
	}

	@Override
	public void onNodeSelected(Node node) {
		Intent i = new Intent();
		i.putExtra(PICKED_NODE, node);
		setResult(RESULT_OK, i);
		finish();
	}
	
	public List<Node> fetchNodes() {
		return mService.fetchNodes();
	}
	
	public void clearNodes() {
		mService.clearNodes();
	}
	
	public void insertNodes(List<Node> nodes) {
		mService.insertNodes(nodes);
	}
}
