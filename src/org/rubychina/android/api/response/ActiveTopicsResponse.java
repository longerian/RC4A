package org.rubychina.android.api.response;

import java.util.ArrayList;
import java.util.List;

import org.rubychina.android.type.Topic;


public class ActiveTopicsResponse extends RCAPIResponse {

	private List<Topic> topics = new ArrayList<Topic>();

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}
	
}
