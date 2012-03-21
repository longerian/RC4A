package org.rubychina.android.activity;

import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.LoaderActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.util.List;

import org.rubychina.android.GlobalResource;
import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.RCService;
import org.rubychina.android.RCService.LocalBinder;
import org.rubychina.android.api.request.ActiveTopicsRequest;
import org.rubychina.android.api.response.ActiveTopicsResponse;
import org.rubychina.android.database.RCDBResolver;
import org.rubychina.android.type.Node;
import org.rubychina.android.type.Topic;
import org.rubychina.android.util.LogUtil;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class TopicsActivity extends GDListActivity {

	private static final String TAG = "TopicsActivity";
	private ActiveTopicsRequest request;
	
	public static final int HOT_TOPICS_NODE_ID = -1;
	
	private RCService mService;
	private boolean isBound = false; 
	
	private LoaderActionBarItem progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addActionBarItem(Type.List, R.id.action_bar_nodes);
		progress = (LoaderActionBarItem) addActionBarItem(Type.Refresh, R.id.action_bar_refresh);
		addActionBarItem(Type.Compose, R.id.action_bar_compose);
		
		List<Topic> cachedTopics = RCDBResolver.INSTANCE.fetchTopics(getApplicationContext());
		GlobalResource.INSTANCE.setCurTopics(cachedTopics);
		refreshPage(cachedTopics);
		startTopicsRequest(HOT_TOPICS_NODE_ID);
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
            getListView().invalidate();
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
		cancelTopicsRequest();
	}
	
	private void refreshPage(List<Topic> topics) {
		TopicAdapter adapter = new TopicAdapter(getApplicationContext(), 
				R.layout.topic_item,
				R.id.title, 
				topics);
		setListAdapter(adapter);
	}
	
	private void startTopicsRequest(int nodeId) {
		if(request == null) {
			request = new ActiveTopicsRequest();
		}
		if(!(nodeId == HOT_TOPICS_NODE_ID)) {
			request.setNodeId(nodeId);
		}
		((RCApplication) getApplication()).getAPIClient().request(request, new HotTopicsCallback());
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
        	startTopicsRequest(HOT_TOPICS_NODE_ID);
        	return true;
        case R.id.action_bar_compose:
        	if(((RCApplication) getApplication()).isLogin()) {
        		i.setClass(getApplicationContext(), TopicEditingActivity.class);
        	} else {
        		i.setClass(getApplicationContext(), UserVerificationActivity.class);
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
		i.putExtra(TopicDetailActivity.POS, position);
		startActivity(i);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == NodesActivity.PICK_NODE) {
			if(resultCode == RESULT_OK) {
				Node n = data.getParcelableExtra(NodesActivity.PICKED_NODE);
				startTopicsRequest(n.getId());
			}
		}
	}

	private class HotTopicsCallback implements ApiCallback<ActiveTopicsResponse> {

		@Override
		public void onException(ApiException e) {
			//TODO
			Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_SHORT).show();
			progress.setLoading(false);
		}

		@Override
		public void onFail(ActiveTopicsResponse r) {
			//TODO
			Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
			progress.setLoading(false);
		}

		@Override
		public void onSuccess(ActiveTopicsResponse r) {
			progress.setLoading(false);
			GlobalResource.INSTANCE.setCurTopics(r.getTopics());
			RCDBResolver.INSTANCE.clearTopics(getApplicationContext());
			RCDBResolver.INSTANCE.insertTopics(getApplicationContext(), r.getTopics());
			refreshPage(r.getTopics());
		}
		
	}
	
	private class TopicAdapter extends ArrayAdapter<Topic> {

		private List<Topic> items;
		private Context context;
		private int resource;
		
		public TopicAdapter(Context context, int resource,
				int textViewResourceId, List<Topic> items) {
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
				viewHolder.gravatar = (ImageView) convertView.findViewById(R.id.gravatar);
				viewHolder.title = (TextView) convertView.findViewById(R.id.title);
				viewHolder.author = (TextView) convertView.findViewById(R.id.author);
				viewHolder.replies = (TextView) convertView.findViewById(R.id.reply_count);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Topic t = items.get(position);
			if(isBound) {
				mService.requestUserAvatar(t.getUser(), viewHolder.gravatar);
			}
			viewHolder.title.setText(t.getTitle());
			viewHolder.author.setText(" >> " + t.getUser().getLogin());
			if(t.getRepliesCount() > 99) {
				viewHolder.replies.setText("99+");
			} else {
				viewHolder.replies.setText(t.getRepliesCount() + "");
			}
			return convertView;
		}
		
		private class ViewHolder {
			
			public ImageView gravatar;
			public TextView title;
			public TextView author;
			public TextView replies;
			
		}
		
	}

}
