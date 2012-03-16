package org.rubychina.android.api.response;

import java.util.ArrayList;
import java.util.List;

import org.rubychina.android.type.Reply;
import org.rubychina.android.type.Topic;

public class TopicDetailResponse extends RCAPIResponse {

	private Topic topic;
	
	private List<Reply> replies = new ArrayList<Reply>();

	public Topic getTopic() {
		return topic;
	}

	public List<Reply> getReplies() {
		return replies;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public void setReplies(List<Reply> replies) {
		this.replies = replies;
	} 
	
}
