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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class UserRelativeTopicListFragment extends SherlockFragment implements TopicActor {

	protected static final String USER = "user";
	
	protected OnTopicSelectedListener listener;
	protected RubyChinaActor rubyChina;
	
	protected ListView topic;
	
	protected User user;
	
	protected boolean isActive = false;
	
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.user_relative_topic_layout, null); 
    	topic = (ListView) view.findViewById(R.id.topics);
    	return view;
	}

	protected class UserRelativeTopicsCallback implements ApiCallback<TopicsResponse> {
		
		@Override
		public void onException(ApiException e) {
			rubyChina.hideIndeterminateProgressBar();
			if(isActive) {
				Toast.makeText(getActivity(), R.string.hint_network_error, Toast.LENGTH_SHORT).show();
			}
		}
	
		@Override
		public void onFail(TopicsResponse r) {
			rubyChina.hideIndeterminateProgressBar();
			if(isActive) {
				Toast.makeText(getActivity(), R.string.hint_loading_data_failed, Toast.LENGTH_SHORT).show();
			}
		}
	
		@Override
		public void onSuccess(TopicsResponse r) {
			rubyChina.hideIndeterminateProgressBar();
			if(isActive) {
				refreshPage(r.getTopics());
			}
		}
		
	}
	
	protected void refreshPage(List<Topic> topics) {
		for (Topic topic : topics) {
			if(topic.getUser() == null) {
				topic.setUser(user);
			}
		}
		UserTopicAdapter adapter = new UserTopicAdapter(this, getActivity(),
				R.layout.user_topic_item,
				R.id.title, 
				topics);
		topic.setAdapter(adapter);
		topic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		        listener.onTopicSelected(((TopicAdapter) (parent.getAdapter())).getItems(), 
		        		position);
			}
		});
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
