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

import java.util.List;

import org.rubychina.android.R;
import org.rubychina.android.activity.RubyChinaActor;
import org.rubychina.android.activity.UserIndexActivity;
import org.rubychina.android.api.response.TopicsResponse;
import org.rubychina.android.type.Topic;
import org.rubychina.android.type.User;
import org.rubychina.android.widget.TopicAdapter;
import org.rubychina.android.widget.UserTopicAdapter;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;

public class UserRelativeTopicListFragment extends SherlockListFragment implements TopicActor {

	protected static final String USER = "user";
	
	protected OnTopicSelectedListener listener;
	protected RubyChinaActor rubyChina;
	
	protected User user;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnTopicSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTopicSelectedListener");
        }
        try {
        	rubyChina = (RubyChinaActor) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement RubyChinaActor");
        }
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getArguments() != null) {
			user = getArguments().getParcelable(USER);
		}
	}

	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		listener.onTopicSelected(((TopicAdapter) l.getAdapter()).getItems(), 
				position);
    }
	
	protected class UserRelativeTopicsCallback implements ApiCallback<TopicsResponse> {
		
		@Override
		public void onException(ApiException e) {
			rubyChina.hideIndeterminateProgressBar();
			Toast.makeText(getActivity(), R.string.hint_network_error, Toast.LENGTH_SHORT).show();
		}
	
		@Override
		public void onFail(TopicsResponse r) {
			rubyChina.hideIndeterminateProgressBar();
			Toast.makeText(getActivity(), R.string.hint_loading_data_failed, Toast.LENGTH_SHORT).show();
		}
	
		@Override
		public void onSuccess(TopicsResponse r) {
			rubyChina.hideIndeterminateProgressBar();
			refreshPage(r.getTopics());
		}
		
	}
	
	protected void refreshPage(List<Topic> topics) {
		UserTopicAdapter adapter = new UserTopicAdapter(this, getActivity(),
				R.layout.user_topic_item,
				R.id.title, 
				topics);
		setListAdapter(adapter);
		getListView().setDivider(getResources().getDrawable(R.drawable.list_divider));
		getListView().setDividerHeight(1);
	}
	
	@Override
	public void visitUserProfile(User u) {
		Intent i = new Intent(getActivity(), UserIndexActivity.class);
		Bundle b = new Bundle();
		b.putParcelable(UserIndexActivity.VIEW_PROFILE, u);
		i.putExtras(b);
		startActivity(i);
	}
	
	@Override
	public void requestUserAvatar(User u, ImageView v, int size) {
		rubyChina.getService().requestUserAvatar(u, v, size);
	}
	
	public interface OnTopicSelectedListener {
        public void onTopicSelected(List<Topic> topics, int position);
    }
	
}
