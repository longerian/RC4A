package org.rubychina.android.activity;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import org.rubychina.android.GlobalResource;
import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.api.request.PostTopicRequest;
import org.rubychina.android.api.response.PostTopicResponse;
import org.rubychina.android.type.Node;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TopicEditingActivity extends GDActivity {

	private EditText title;
	private Spinner nodeSelector;
	private EditText body;
	
	private PostTopicRequest request;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setActionBarContentView(R.layout.topic_editing_layout);
		addActionBarItem(Type.Add, R.id.action_bar_add);
		
		title = (EditText) findViewById(R.id.title);
		
		nodeSelector = (Spinner) findViewById(R.id.node);
		ArrayAdapter<Node> adapter = new ArrayAdapter<Node>(getApplicationContext(), 
				android.R.layout.simple_spinner_item, 
				GlobalResource.INSTANCE.getNodes());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		nodeSelector.setAdapter(adapter);
		
		body = (EditText) findViewById(R.id.body);
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
        case R.id.action_bar_add:
        	if(isTopicValid()) {
        		startPostTopicRequest(
        				title.getText().toString(),
        				((Node) nodeSelector.getSelectedItem()).getId() + "",
        				body.getText().toString()
        				);
        	}
        	return true;
        default:
            return super.onHandleActionBarItemClick(item, position);
		}
	}
	
	private boolean isTopicValid() {
		if(TextUtils.isEmpty(title.getText().toString())) {
			Toast.makeText(getApplicationContext(), "title empty", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(TextUtils.isEmpty(body.getText().toString())) {
			Toast.makeText(getApplicationContext(), "body empty", Toast.LENGTH_SHORT).show();
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
		setProgressBarIndeterminateVisibility(true);
	}
	
	private class PostTopicCallback implements ApiCallback<PostTopicResponse> {

		@Override
		public void onException(ApiException e) {
			//TODO
			Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_SHORT).show();
			setProgressBarIndeterminateVisibility(false);
			e.printStackTrace();
		}

		@Override
		public void onFail(PostTopicResponse r) {
			// TODO Auto-generated method stub
			setProgressBarIndeterminateVisibility(false);
		}

		@Override
		public void onSuccess(PostTopicResponse r) {
			setProgressBarIndeterminateVisibility(false);
			Intent homeIntent = new Intent(TopicEditingActivity.this, 
					((RCApplication) getApplicationContext()).getHomeActivityClass());
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
		}
		
	}
	
}
