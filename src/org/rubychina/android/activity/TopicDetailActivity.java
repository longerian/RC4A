package org.rubychina.android.activity;

import greendroid.app.GDActivity;

import org.rubychina.android.RCApplication;
import org.rubychina.android.api.request.TopicDetailRequest;
import org.rubychina.android.api.response.TopicDetailResponse;
import org.rubychina.android.util.LogUtil;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class TopicDetailActivity extends GDActivity {

	private static final String TAG = "TopicDetailActivity";
	private TopicDetailRequest request;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		startTopicDetailRequest(getIntent().getIntExtra("id", 0));
	}
	
	private void startTopicDetailRequest(int id) {
		if(request == null) {
			request = new TopicDetailRequest(id);
		}
		request.setId(id);
		((RCApplication) getApplication()).getAPIClient().request(request, new TopicDetailCallback());
		setProgressBarIndeterminateVisibility(true);
	}
	
	private class TopicDetailCallback implements ApiCallback<TopicDetailResponse> {

		@Override
		public void onException(ApiException e) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onFail(TopicDetailResponse r) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onSuccess(TopicDetailResponse r) {
			LogUtil.d(TAG, r.getTopic() + "");
		}
		
	}
	
}
