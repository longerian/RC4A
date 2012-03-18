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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TopicEditingActivity extends GDActivity {

	private static final int DIALOG_EXIT = 1;
	
	private EditText title;
	private Spinner nodeSelector;
	private EditText body;
	
	private ProgressDialog sending;
	
	private PostTopicRequest request;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	switch(id) {
    	case DIALOG_EXIT:
    		builder.setMessage(R.string.hint_stoping_editing_topic_or_not)
    		.setCancelable(false)
    		.setPositiveButton(R.string.p_yek, new OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				TopicEditingActivity.this.finish();
    				
    			}
    		})
    		.setNegativeButton(R.string.p_no, new OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.cancel();
    			}
    		});
    		break;
    	default:
    		break;
    	}
    	AlertDialog alert = builder.create();
		return alert;
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
		showProgress();
	}
	
	private void showProgress() {
		if(sending == null) {
			sending = new ProgressDialog(this);
			sending.setCancelable(false);
		}
		sending.setMessage(getString(R.string.hint_submiting_topic));
		sending.show();
	}
	
	private void dismissProgress() {
		if(sending != null) {
			sending.dismiss();
		}
	}
	
	private class PostTopicCallback implements ApiCallback<PostTopicResponse> {

		@Override
		public void onException(ApiException e) {
			dismissProgress();
			Toast.makeText(getApplicationContext(), 
					R.string.hint_network_or_token_problem, 
					Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		}

		@Override
		public void onFail(PostTopicResponse r) {
			dismissProgress();
			Toast.makeText(getApplicationContext(), 
					R.string.hint_network_or_token_problem, 
					Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onSuccess(PostTopicResponse r) {
			dismissProgress();
			Intent homeIntent = new Intent(TopicEditingActivity.this, 
					((RCApplication) getApplicationContext()).getHomeActivityClass());
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(!TextUtils.isEmpty(title.getText().toString())
					|| !TextUtils.isEmpty(body.getText().toString())) {
				showDialog(DIALOG_EXIT);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
