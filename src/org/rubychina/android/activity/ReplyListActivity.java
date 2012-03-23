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

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.LoaderActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.RCService;
import org.rubychina.android.RCService.LocalBinder;
import org.rubychina.android.api.request.TopicDetailRequest;
import org.rubychina.android.api.response.TopicDetailResponse;
import org.rubychina.android.type.Reply;
import org.rubychina.android.util.GravatarUtil;
import org.rubychina.android.util.ImageParser;
import org.rubychina.android.util.LogUtil;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class ReplyListActivity extends GDActivity {

	public static final String BELONG_TO_TOPIC = "org.rubychina.android.activity.ReplyListActivity.TOPIC_ID";
	
	private static final String TAG = "ReplyListActivity";
	
	private static final int VIEW_PROFILE = 0; 
	
	private RCService mService;
	private boolean isBound = false; 
	private ImageParser ip;
	
	private TopicDetailRequest request;
	private ListView replies;
	
	private LoaderActionBarItem progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setActionBarContentView(R.layout.topic_replies_layout);
		progress = (LoaderActionBarItem) addActionBarItem(Type.Refresh, R.id.action_bar_refresh);
		ip = new ImageParser(getApplicationContext());
		startTopicDetailRequest(getIntent().getIntExtra(BELONG_TO_TOPIC, 0));
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
		cancelTopicDetailRequest();
	}
	
	private void startTopicDetailRequest(int id) {
		if(request == null) {
			request = new TopicDetailRequest(id);
		}
		request.setId(id);
		((RCApplication) getApplication()).getAPIClient().request(request, new TopicDetailCallback());
		progress.setLoading(true);
	}
	
	private void cancelTopicDetailRequest() {
		if(request != null) {
			((RCApplication) getApplication()).getAPIClient().cancel(request);
			progress.setLoading(false);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, VIEW_PROFILE, 0, R.string.action_view_profile);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {  
		  case VIEW_PROFILE:
			  Gson g = new Gson();
			  Reply r = (Reply) replies.getItemAtPosition(menuInfo.position);
			  Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
			  i.putExtra(UserProfileActivity.VIEW_PROFILE, g.toJson(r.getUser()));
			  startActivity(i);
			  return true;  
		  default:  
			  return super.onContextItemSelected(item);  
		  }  
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
        case R.id.action_bar_refresh:
        	startTopicDetailRequest(getIntent().getIntExtra(BELONG_TO_TOPIC, 0));
        	return true;
        default:
            return super.onHandleActionBarItemClick(item, position);
		}
	}

	private class TopicDetailCallback implements ApiCallback<TopicDetailResponse> {

		@Override
		public void onException(ApiException e) {
			Toast.makeText(getApplicationContext(), R.string.hint_loading_data_failed, Toast.LENGTH_SHORT).show();
			progress.setLoading(false);
		}

		@Override
		public void onFail(TopicDetailResponse r) {
			Toast.makeText(getApplicationContext(), R.string.hint_loading_data_failed, Toast.LENGTH_SHORT).show();
			progress.setLoading(false);
		}

		@Override
		public void onSuccess(TopicDetailResponse r) {
			progress.setLoading(false);
			replies = (ListView) findViewById(R.id.replies);
			List<Reply> rs = r.getReplies();
			if(!rs.isEmpty()) {
				Collections.sort(r.getReplies());
				replies.setAdapter(new ReplyAdapter(getApplicationContext(), R.layout.reply_item,
						R.id.body, r.getReplies()));
				registerForContextMenu(replies);
			} else {
				Toast.makeText(getApplicationContext(), R.string.hint_no_replies, Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	private class ReplyAdapter extends ArrayAdapter<Reply> {

		private List<Reply> items;
		private Context context;
		private int resource;
		
		public ReplyAdapter(Context context, int resource,
				int textViewResourceId, List<Reply> items) {
			super(context, resource, textViewResourceId, items);
			this.context = context;
			this.resource = resource;
			this.items = items;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			if(convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(resource, null);
				viewHolder.gravatar = (ImageView) convertView.findViewById(R.id.gravatar);
				viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
				viewHolder.floor = (TextView) convertView.findViewById(R.id.floor);
				viewHolder.body = (TextView) convertView.findViewById(R.id.body);
				viewHolder.forward = (ImageView) convertView.findViewById(R.id.forward);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Reply r = items.get(position);
			if(isBound) {
				mService.requestUserAvatar(r.getUser(), viewHolder.gravatar, 0);
			}
			viewHolder.userName.setText(r.getUser().getLogin());
			viewHolder.floor.setText(position + 1 + "" + getString(R.string.reply_list_unit));
			viewHolder.body.setText(ip.replace(r.getBody()));
			return convertView;
		}
		
		private class ViewHolder {
			
			public ImageView gravatar;
			public TextView userName;
			public TextView floor;
			public TextView body;
			public ImageView forward;
			
		}
		
	}
	
}
