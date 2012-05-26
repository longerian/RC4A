package org.rubychina.android.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.rubychina.android.R;
import org.rubychina.android.activity.RubyChinaActor;
import org.rubychina.android.api.request.NodesRequest;
import org.rubychina.android.api.response.NodesResponse;
import org.rubychina.android.type.Node;
import org.rubychina.android.type.NodeSection;
import org.rubychina.android.widget.NodeAdapter;
import org.rubychina.android.widget.SeparatedListAdapter;

import yek.api.ApiCallback;
import yek.api.ApiException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;

public class NodeListFragment extends SherlockListFragment {

	private OnNodeSelectedListener listener;
	private RubyChinaActor rubyChina;
	private NodesRequest request;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnNodeSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnNodeSelectedListener");
        }
        try {
        	rubyChina = (RubyChinaActor) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement RubyChinaActor");
        }
    }
	 
    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		List<Node> nodes = fetchNodes();
		if(nodes.isEmpty()) {
			startNodesRequest();
		} else {
			refreshPage(nodes);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		cancelNodesRequest();
	}

	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Node n = (Node) l.getItemAtPosition(position);
        listener.onNodeSelected(n);
	}
	
	private List<Node> fetchNodes() {
		return rubyChina.getService().fetchNodes();
	}
	
	public void startNodesRequest() {
		if(request == null) {
			request = new NodesRequest();
		}
		rubyChina.getClient().request(request, new NodesCallback());
		rubyChina.showIndeterminateProgressBar();
	}
	
	private void cancelNodesRequest() {
		if(request != null) {
			rubyChina.getClient().cancel(request);
		}
		rubyChina.hideIndeterminateProgressBar();
	}
	
	private void refreshPage(List<Node> nodes) {
		if(!nodes.contains(Node.MOCK_ACTIVE_NODE)) {
			nodes.add(0, Node.MOCK_ACTIVE_NODE);
		}
		Map<NodeSection, List<Node>> groupedNodes = new TreeMap<NodeSection, List<Node>>();
		for(Node n : nodes) {
			NodeSection s = n.whichSection();
			if(groupedNodes.containsKey(s)) {
				groupedNodes.get(s).add(n);
			} else {
				List<Node> ns = new ArrayList<Node>();
				ns.add(n);
				groupedNodes.put(s, ns);
			}
		}
		SeparatedListAdapter adapter = new SeparatedListAdapter(getActivity());
		Iterator<Entry<NodeSection, List<Node>>> iter = groupedNodes.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<NodeSection, List<Node>> entry = (Entry<NodeSection, List<Node>>) iter.next();
			adapter.addSection(entry.getKey(), 
					new NodeAdapter(getActivity(), R.layout.node_item, entry.getValue()));
		}
		setListAdapter(adapter);
		getListView().setDivider(getResources().getDrawable(R.drawable.list_divider));
		getListView().setDividerHeight(1);
	}
	
	private class NodesCallback implements ApiCallback<NodesResponse> {

		@Override
		public void onException(ApiException e) {
			rubyChina.hideIndeterminateProgressBar();
			Toast.makeText(getActivity(), R.string.hint_network_error, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onFail(NodesResponse r) {
			rubyChina.hideIndeterminateProgressBar();
			Toast.makeText(getActivity(), R.string.hint_loading_data_failed, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onSuccess(NodesResponse r) {
			rubyChina.hideIndeterminateProgressBar();
			refreshPage(r.getNodes());
			new CacheNodesTask().execute(r.getNodes());
		}
		
	}
	
	private class CacheNodesTask extends AsyncTask<List<Node>, Void, Void> {

		@Override
		protected void onPreExecute() {
			rubyChina.showIndeterminateProgressBar();
		}

		@Override
		protected Void doInBackground(List<Node>... params) {
			rubyChina.getService().clearNodes();
			rubyChina.getService().insertNodes(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			rubyChina.hideIndeterminateProgressBar();
		}
		
	}
	
	public interface OnNodeSelectedListener {
        public void onNodeSelected(Node node);
    }
	  
}
