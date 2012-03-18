package org.rubychina.android;

import java.util.ArrayList;
import java.util.List;

import org.rubychina.android.type.Node;
import org.rubychina.android.type.Topic;

/**
 * @author Longer
 *
 */
public enum GlobalResource {

	INSTANCE;
	
	/**
	 * current topic list
	 */
	private List<Topic> curTopics = new ArrayList<Topic>();
	
	private List<Node> nodes = new ArrayList<Node>();
	
	public List<Topic> getCurTopics() {
		return curTopics;
	}

	public void setCurTopics(List<Topic> curTopics) {
		this.curTopics = curTopics;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
	
}
