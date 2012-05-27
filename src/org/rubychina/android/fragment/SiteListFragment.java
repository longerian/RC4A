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
import org.rubychina.android.api.request.SitesRequest;
import org.rubychina.android.api.response.SitesResponse;
import org.rubychina.android.type.Site;
import org.rubychina.android.type.SiteGroup;
import org.rubychina.android.widget.SiteAdapter;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class SiteListFragment extends SherlockFragment {

	private OnSiteSelectedListener listener;
	private RubyChinaActor rubyChina;
	private SitesRequest request;
	
	private ExpandableListView sitesList;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnSiteSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSiteSelectedListener");
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
    	View view = inflater.inflate(R.layout.sites_layout, null);
    	sitesList = (ExpandableListView) view.findViewById(R.id.sites);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		List<SiteGroup> cachedSites = fetchSites();
		refreshPage(cachedSites);
		startSitesRequest();
	}

	private List<SiteGroup> fetchSites() {
		return rubyChina.getService().fetchSites();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		cancelSitesRequest();
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
	
	public void startSitesRequest() {
		if(request == null) {
			request = new SitesRequest();
		}
		rubyChina.getClient().request(request, new SitesCallback());
		rubyChina.showIndeterminateProgressBar();
	}
	
	private void cancelSitesRequest() {
		if(request != null) {
			rubyChina.getClient().cancel(request);
		}
		rubyChina.hideIndeterminateProgressBar();
	}
	
	private class SitesCallback implements ApiCallback<SitesResponse> {

		@Override
		public void onException(ApiException e) {
			rubyChina.hideIndeterminateProgressBar();
			Toast.makeText(getActivity(), R.string.hint_network_error, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onFail(SitesResponse r) {
			rubyChina.hideIndeterminateProgressBar();
			Toast.makeText(getActivity(), R.string.hint_loading_data_failed, Toast.LENGTH_SHORT).show();
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(SitesResponse r) {
			rubyChina.hideIndeterminateProgressBar();
			refreshPage(r.getSiteGroups());
			new CacheSitesTask().execute(r.getSiteGroups());
		}
		
	}
	
	private void refreshPage(List<SiteGroup> sites) {
		sitesList.setAdapter(new SiteAdapter(this, sites, R.layout.site_group_item, R.layout.site_child_item));
		sitesList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Site site = (Site) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
				listener.onSiteSelected(site);
				return true;
			}
		});
	}
	
	private class CacheSitesTask extends AsyncTask<List<SiteGroup>, Void, Void> {

		@Override
		protected void onPreExecute() {
			rubyChina.showIndeterminateProgressBar();
		}

		@Override
		protected Void doInBackground(List<SiteGroup>... params) {
			rubyChina.getService().clearSites();
			rubyChina.getService().insertSites(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			rubyChina.hideIndeterminateProgressBar();
		}
		
	}
	
	public OnSiteSelectedListener getListener() {
		return listener;
	}

	public void requestImage(String url, ImageView view) {
		rubyChina.getService().requestImage(url, view);
	}
		
	public interface OnSiteSelectedListener {
		
		public void onSiteSelected(Site site);
		
		public void onSiteDescSelected(Site site);
		
	}
	
}
