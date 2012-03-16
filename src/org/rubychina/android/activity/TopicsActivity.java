package org.rubychina.android.activity;

import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.util.List;

import org.rubychina.android.GlobalResource;
import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.api.request.HotTopicsRequest;
import org.rubychina.android.api.response.HotTopicsResponse;
import org.rubychina.android.type.Topic;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
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
	private HotTopicsRequest request;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		addActionBarItem(Type.Refresh, R.id.action_bar_refresh);
		addActionBarItem(Type.Compose, R.id.action_bar_compose);

		startTopicsRequest();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		cancelTopicsRequest();
	}
	
	private void startTopicsRequest() {
		if(request == null) {
			request = new HotTopicsRequest();
		}
		((RCApplication) getApplication()).getAPIClient().request(request, new HotTopicsCallback());
		setProgressBarIndeterminateVisibility(true);
	}
	
	private void cancelTopicsRequest() {
		if(request != null) {
			((RCApplication) getApplication()).getAPIClient().cancel(request);
			setProgressBarIndeterminateVisibility(false);
		}
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
        case R.id.action_bar_refresh:
        	startTopicsRequest();
        	return true;
        case R.id.action_bar_compose:
        	Intent i = new Intent();
        	i.setClass(getApplicationContext(), NewTopicActivity.class);
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

	private class HotTopicsCallback implements ApiCallback<HotTopicsResponse> {

		@Override
		public void onException(ApiException e) {
			//TODO
			Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_SHORT).show();
			setProgressBarIndeterminateVisibility(false);
		}

		@Override
		public void onFail(HotTopicsResponse r) {
			//TODO
			Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
			setProgressBarIndeterminateVisibility(false);
		}

		@Override
		public void onSuccess(HotTopicsResponse r) {
			setProgressBarIndeterminateVisibility(false);
			GlobalResource.INSTANCE.setCurTopics(r.getTopics());
			TopicAdapter adapter = new TopicAdapter(getApplicationContext(), R.layout.topic_item,
					R.id.title, r.getTopics());
			setListAdapter(adapter);
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
				viewHolder.replies = (TextView) convertView.findViewById(R.id.reply_count);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Topic t = items.get(position);
			String avatar = t.getUser().getAvatarUrl();
			if(TextUtils.isEmpty(avatar)) {
				viewHolder.gravatar.setImageResource(R.drawable.default_gravatar);
			} else {
				Bitmap ava = ((RCApplication) getApplication()).getImgLoader().load(avatar, viewHolder.gravatar);
				if(ava != null) {
					viewHolder.gravatar.setImageBitmap(ava);
				} else {
					viewHolder.gravatar.setImageResource(R.drawable.default_gravatar);
				}
			}
			viewHolder.title.setText(t.getTitle());
			viewHolder.replies.setText(t.getRepliesCount() + "");
			return convertView;
		}
		
		private class ViewHolder {
			
			public ImageView gravatar;
			public TextView title;
			public TextView replies;
			
		}
		
	}

}
