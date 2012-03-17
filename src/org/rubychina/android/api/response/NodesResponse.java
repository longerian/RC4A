package org.rubychina.android.api.response;

import java.util.ArrayList;
import java.util.List;

import org.rubychina.android.type.Node;

public class NodesResponse extends RCAPIResponse {

	private List<Node> nodes = new ArrayList<Node>();

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
	
}
