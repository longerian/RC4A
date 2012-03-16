package org.rubychina.android;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rubychina.android.type.Reply;
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
	
	/**
	 * cache pool for replis of topics
	 */
	private Map<Topic, SoftReference<List<Reply>>> topicReplies = new HashMap<Topic, SoftReference<List<Reply>>>();

	public List<Topic> getCurTopics() {
		return curTopics;
	}

	public void setCurTopics(List<Topic> curTopics) {
		this.curTopics = curTopics;
	}

	public Map<Topic, SoftReference<List<Reply>>> getTopicReplies() {
		return topicReplies;
	}

	public void setTopicReplies(
			Map<Topic, SoftReference<List<Reply>>> topicReplies) {
		this.topicReplies = topicReplies;
	}
	
	
	
}
