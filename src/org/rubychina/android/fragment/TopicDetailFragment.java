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
package org.rubychina.android.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rubychina.android.R;
import org.rubychina.android.RCApplication;
import org.rubychina.android.activity.TopicDetailActivity;
import org.rubychina.android.activity.UserProfileActivity;
import org.rubychina.android.api.request.TopicDetailRequest;
import org.rubychina.android.api.response.TopicDetailResponse;
import org.rubychina.android.type.Reply;
import org.rubychina.android.type.Topic;
import org.rubychina.android.type.User;
import org.rubychina.android.util.JsonUtil;
import org.rubychina.android.widget.ReplyAdapter;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;

public class TopicDetailFragment extends SherlockListFragment {
	
	public static final String TOPIC = "topic";

	private TopicDetailActivity hostActivity;
	private Topic topic;
	private TopicDetailRequest request;
	
	private ImageView gravatar;
	
	public static TopicDetailFragment newInstance(Topic topic) {
		TopicDetailFragment f = new TopicDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TOPIC, topic);
        f.setArguments(bundle);
        return f;
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
        	topic = getArguments().getParcelable(TOPIC);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hostActivity = (TopicDetailActivity) getActivity();
        startTopicDetailRequest(topic);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "Item clicked: " + id);
    }
    
    public void startTopicDetailRequest(Topic topic) {
		if(request == null) {
			request = new TopicDetailRequest(topic.getId());
		}
		request.setId(topic.getId());
		((RCApplication) hostActivity.getApplication()).getAPIClient().request(request, new TopicDetailCallback());
		hostActivity.setSupportProgressBarIndeterminateVisibility(true);
	}
	
	private class TopicDetailCallback implements ApiCallback<TopicDetailResponse> {

		@Override
		public void onException(ApiException e) {
			hostActivity.setSupportProgressBarIndeterminateVisibility(false);
			Toast.makeText(hostActivity, R.string.hint_network_error, Toast.LENGTH_SHORT).show();
			refreshView(new ArrayList<Reply>());
		}

		@Override
		public void onFail(TopicDetailResponse r) {
			hostActivity.setSupportProgressBarIndeterminateVisibility(false);
			Toast.makeText(hostActivity, R.string.hint_loading_data_failed, Toast.LENGTH_SHORT).show();
			refreshView(new ArrayList<Reply>());
		}

		@Override
		public void onSuccess(TopicDetailResponse r) {
			hostActivity.setSupportProgressBarIndeterminateVisibility(false);
			refreshView(r.getReplies());
		}
		
	}
	
	private void refreshView(List<Reply> rs) {
		getListView().addHeaderView(initializeTopicBody(), null, false);
		Collections.sort(rs);
		setListAdapter(new ReplyAdapter(this, R.layout.reply_item,
				R.id.body, rs));
	}
	
	private View initializeTopicBody() {
		View body = LayoutInflater.from(hostActivity).inflate(R.layout.topic_body_layout, null);
		
		gravatar = (ImageView) body.findViewById(R.id.gravatar);
		gravatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				visitUserProfile(topic.getUser());
			}
		});
		requestUserAvatar(topic.getUser(), gravatar, 0);
		
		TextView title = (TextView) body.findViewById(R.id.title);
		title.setText(topic.getTitle());
		
		TextView bodyText = (TextView) body.findViewById(R.id.body);
		executeRetrieveSpannedTask(bodyText, topic.getBodyHTML());
		return body;
	}
	
	public void visitUserProfile(User u) {
		Intent i = new Intent(hostActivity, UserProfileActivity.class);
		i.putExtra(UserProfileActivity.VIEW_PROFILE, JsonUtil.toJsonObject(u));
		startActivity(i);
	}
	
	public void requestUserAvatar(User u, ImageView v, int size) {
		hostActivity.requestUserAvatar(u, v, size);
	}
	
	public void executeRetrieveSpannedTask(TextView tv, String html) {
		new RetrieveSpannedTask(tv).execute(html);
	}
	
	private class RetrieveSpannedTask extends AsyncTask<String, Void, Spanned> {

		private TextView htmlView;
		
		public RetrieveSpannedTask(TextView htmlView) {
			this.htmlView = htmlView;
		}
		
		@Override
		protected void onPreExecute() {
			hostActivity.setSupportProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Spanned doInBackground(String... params) {
			return Html.fromHtml(params[0], hostActivity.getImageGetter(), null);
		}
		
		@Override
		protected void onPostExecute(Spanned result) {
			hostActivity.setSupportProgressBarIndeterminateVisibility(false);
			htmlView.setText(result);
		}
		
	}
    
}
