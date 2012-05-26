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

import org.rubychina.android.api.request.UserFavoriteTopicsRequest;
import org.rubychina.android.type.User;

import android.os.Bundle;

public class UserFavoriteTopicListFragment extends
		UserRelativeTopicListFragment {

	private UserFavoriteTopicsRequest request;
	
	public static UserFavoriteTopicListFragment newInstance(User user) {
		UserFavoriteTopicListFragment f = new UserFavoriteTopicListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(USER, user);
        f.setArguments(bundle);
        return f;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		startTopicsRequest(user);
	}

    @Override
	public void onDestroy() {
		super.onDestroy();
		cancelTopicsRequest();
	}

	public void startTopicsRequest(User user) {
		if(request == null) {
			request = new UserFavoriteTopicsRequest();
		}
		request.setLogin(user.getLogin());
		rubyChina.getClient().request(request, new UserRelativeTopicsCallback());
		rubyChina.showIndeterminateProgressBar();
	}
	
	private void cancelTopicsRequest() {
		if(request != null) {
			rubyChina.getClient().cancel(request);
			rubyChina.hideIndeterminateProgressBar();
		}
	}
	
}
