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
//	private static final int REPLY = 1; 
	
	private RCService mService;
	private boolean isBound = false; 
	private ImageParser ip;
	
	private TopicDetailRequest request;
	private ListView replies;
//	private EditText replyContent;
	
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
	
	//no api for reply, so remove reply box
//	@Override
//	protected void onStart() {
//		super.onStart();
//		View replyBox = findViewById(R.id.comment);
//		View loginNoteBox = findViewById(R.id.login_note);
//		if(((RCApplication) getApplication()).isLogin()) {
//			replyBox.setVisibility(View.VISIBLE);
//			loginNoteBox.setVisibility(View.INVISIBLE);
//			replyContent = (EditText) findViewById(R.id.content);
//			ImageView submit = (ImageView) findViewById(R.id.submit);
//			submit.setOnClickListener(mSubmitListener);
//		} else {
//			replyBox.setVisibility(View.INVISIBLE);
//			loginNoteBox.setVisibility(View.VISIBLE);
//			loginNoteBox.setOnClickListener(mNeedLoginListener);
//		}
//	}
	
//	private OnClickListener mNeedLoginListener = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			Intent i = new Intent(getApplicationContext(), UserVerificationActivity.class);
//			startActivity(i);
//		}
//	};
//	
//	private OnClickListener mSubmitListener = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			Intent i = new Intent();
//			Toast.makeText(getApplicationContext(), "submit: " + replyContent.getText().toString(), Toast.LENGTH_SHORT).show();
//		}
//	};
	
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
//		menu.add(0, REPLY, 1, R.string.action_reply);
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
//		  case REPLY:  
//			  Toast.makeText(ReplyListActivity.this, "reply to " + replies.getItemAtPosition(menuInfo.position), Toast.LENGTH_LONG).show();
//			  return true;
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
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_SHORT).show();
			progress.setLoading(false);
		}

		@Override
		public void onFail(TopicDetailResponse r) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
			progress.setLoading(false);
		}

		@Override
		public void onSuccess(TopicDetailResponse r) {
			progress.setLoading(false);
			replies = (ListView) findViewById(R.id.replies);
			Collections.sort(r.getReplies());
			replies.setAdapter(new ReplyAdapter(getApplicationContext(), R.layout.reply_item,
					R.id.body, r.getReplies()));
			registerForContextMenu(replies);
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
				mService.requestUserAvatar(r.getUser(), viewHolder.gravatar);
			}
			viewHolder.userName.setText(r.getUser().getLogin());
			viewHolder.floor.setText(position + 1 + "" + getString(R.string.reply_list_unit));
			viewHolder.body.setText(ip.replace(r.getBody()));
			
//			viewHolder.forward.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//				}
//			});
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
