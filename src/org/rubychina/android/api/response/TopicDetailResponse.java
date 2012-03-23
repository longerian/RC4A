/*Copyright (C) 2010 Longerian (http://www.longerian.me)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
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
