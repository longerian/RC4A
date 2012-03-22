package org.rubychina.android.activity;

import java.util.List;

import org.rubychina.android.GlobalResource;
import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.RCService;
import org.rubychina.android.RCService.LocalBinder;
import org.rubychina.android.api.request.NodesRequest;
import org.rubychina.android.api.response.NodesResponse;
import org.rubychina.android.type.Node;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;

public class RCActivity extends Activity {

	private static final String TAG = "RCActivity";
	private NodesRequest request;
	private RCService mService;
	private boolean isBound = false; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rc_layout);
		WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        ((RCApplication) getApplication()).setScreenWidth(display.getWidth());
        ((RCApplication) getApplication()).setScreenHeight(display.getHeight());
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}
	
	private void initialize() {
		List<Node> nodes = mService.fetchNodes();
		if(nodes.isEmpty()) {
			startNodesRequest();
		} else {
			new CountDownTimer(1000, 500) {
				
				@Override
				public void onTick(long millisUntilFinished) {
				}
				
				@Override
				public void onFinish() {
					go2Topics();
				}
				
			}.start();
		}
	}
	
	private void go2Topics() {
		Intent i = new Intent();
		i.setClass(getApplicationContext(), TopicsActivity.class);
		startActivity(i);
		finish();
	}
	
	private void startNodesRequest() {
		if(request == null) {
			request = new NodesRequest();
		}
		((RCApplication) getApplication()).getAPIClient().request(request, new NodesCallback());
	}
	
	private class NodesCallback implements ApiCallback<NodesResponse> {

		@Override
		public void onException(ApiException e) {
			go2Topics();
		}

		@Override
		public void onFail(NodesResponse r) {
			go2Topics();
		}

		@Override
		public void onSuccess(NodesResponse r) {
			mService.clearNodes();
			mService.insertNodes(r.getNodes());
			go2Topics();
		}
		
	}

}
