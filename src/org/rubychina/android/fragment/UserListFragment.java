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
import org.rubychina.android.api.request.UsersRequest;
import org.rubychina.android.api.response.UsersResponse;
import org.rubychina.android.type.User;
import org.rubychina.android.widget.UserAdapter;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class UserListFragment extends SherlockFragment {

	private OnUserSelectedListener listener;
	private RubyChinaActor rubyChina;
	private UsersRequest request;
	
	private GridView usersGrid;
	
	private boolean isActive = false;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnUserSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnUserSelectedListener");
        }
        try {
        	rubyChina = (RubyChinaActor) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement RubyChinaActor");
        }
    }
	 
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.users_layout, null);
    	usersGrid = (GridView) view.findViewById(R.id.users);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		List<User> cachedUsers = fetchUsers();
		refreshPage(cachedUsers);
		startUsersRequest();
		isActive = true;
	}

	private List<User> fetchUsers() {
		return rubyChina.getService().fetchUsers();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		isActive = false;
		cancelUsersRequest();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, R.id.action_bar_compose, 1, R.string.actionbar_compose)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(0, R.id.action_bar_setting, 2, R.string.actionbar_setting)
        	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
        case R.id.action_bar_compose:
        	rubyChina.onCompose();
        	break;
        case R.id.action_bar_setting:
        	rubyChina.onSetting();
			break;
		default: 
			break;
		}
		return true;
	}
	
	public void startUsersRequest() {
		if(request == null) {
			request = new UsersRequest();
		}
		rubyChina.getClient().request(request, new UsersCallback());
		rubyChina.showIndeterminateProgressBar();
	}
	
	private void cancelUsersRequest() {
		if(request != null) {
			rubyChina.getClient().cancel(request);
		}
		rubyChina.hideIndeterminateProgressBar();
	}
	
	public interface OnUserSelectedListener {
        public void onUserSelected(User user);
    }
	
	private class UsersCallback implements ApiCallback<UsersResponse> {

		@Override
		public void onException(ApiException e) {
			rubyChina.hideIndeterminateProgressBar();
			if(isActive) {
				Toast.makeText(getActivity(), R.string.hint_network_error, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onFail(UsersResponse r) {
			rubyChina.hideIndeterminateProgressBar();
			if(isActive) {
				Toast.makeText(getActivity(), R.string.hint_loading_data_failed, Toast.LENGTH_SHORT).show();
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(UsersResponse r) {
			rubyChina.hideIndeterminateProgressBar();
			if(isActive) {
				refreshPage(r.getUsers());
			}
			new CacheUsersTask().execute(r.getUsers());
		}
		
	}
	
	private void refreshPage(List<User> users) {
		usersGrid.setAdapter(new UserAdapter(this, R.layout.user_item, R.id.login, users));
		usersGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		        User user = (User) parent.getItemAtPosition(position);
		        listener.onUserSelected(user);
			}
		});
	}
	
	private class CacheUsersTask extends AsyncTask<List<User>, Void, Void> {

		@Override
		protected void onPreExecute() {
			rubyChina.showIndeterminateProgressBar();
		}

		@Override
		protected Void doInBackground(List<User>... params) {
			rubyChina.getService().clearUsers();
			rubyChina.getService().insertUsers(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			rubyChina.hideIndeterminateProgressBar();
		}
		
	}

	public void requestUserAvatar(User u, ImageView gravatar) {
		int size = (rubyChina.getApp().getScreenWidth() - 2) / 3;
		rubyChina.getService().requestUserAvatar(u, gravatar, size);
	}

}
