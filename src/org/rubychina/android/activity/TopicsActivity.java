package org.rubychina.android.activity;

import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ItemAdapter;
import greendroid.widget.ActionBarItem.Type;

import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.api.request.HotTopicsRequest;
import org.rubychina.android.api.response.HotTopicsResponse;
import org.rubychina.android.type.Topic;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class TopicsActivity extends GDListActivity {

	private static final String TAG = "TopicsActivity";
	private HotTopicsRequest request;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addActionBarItem(Type.Refresh, R.id.action_bar_refresh);
		addActionBarItem(Type.Compose, R.id.action_bar_compose);
		
		request = new HotTopicsRequest();
		((RCApplication) getApplication()).getAPIClient().request(request, new HotTopicsCallback());
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
        case R.id.action_bar_refresh:
        	Toast.makeText(getApplicationContext(), "refresh", Toast.LENGTH_SHORT).show();
        	return true;
        case R.id.action_bar_compose:
        	Intent i = new Intent();
        	i.setClass(getApplicationContext(), NewTopicActivity.class);
        	startActivity(i);
        	return true;
        default:
        	Toast.makeText(getApplicationContext(), "default", Toast.LENGTH_SHORT).show();
            return super.onHandleActionBarItemClick(item, position);
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(getApplicationContext(), "click " + position, Toast.LENGTH_SHORT).show();
	}

	private class HotTopicsCallback implements ApiCallback<HotTopicsResponse> {

		@Override
		public void onException(ApiException e) {
			Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onFail(HotTopicsResponse r) {
			Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onSuccess(HotTopicsResponse r) {
			ArrayAdapter<Topic> adapter = new ArrayAdapter<Topic>(getApplicationContext(), android.R.layout.simple_list_item_1);
			for(Topic t : r.getTopics()) {
				adapter.add(t);
			}
			setListAdapter(adapter);
		}
		
	}

}
