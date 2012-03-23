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
package org.rubychina.android.api.request;

import java.util.HashMap;
import java.util.Map;

import org.rubychina.android.activity.TopicsActivity;
import org.rubychina.android.api.RCAPIContext;
import org.rubychina.android.api.response.ActiveTopicsResponse;

import yek.cache.Cache;

public class ActiveTopicsRequest extends RCAPIGet<ActiveTopicsResponse> {

	private static final String TAG = "HotTopicsRequest";
	
	private static final int DEFAULT_SIZE = 30;
	
	private static final String SIZE_KEY = "size";
	
	private int size = DEFAULT_SIZE;
	private int nodeId = TopicsActivity.HOT_TOPICS_NODE_ID;
	
	public void setSize(int size) {
		if(size < 0 || size > 100) {
			this.size = DEFAULT_SIZE;
		} else {
			this.size = size; 
		}
	}
	
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	
	@Override
	public String getRequestURL(RCAPIContext context) {
		if(nodeId == TopicsActivity.HOT_TOPICS_NODE_ID) {
			return context.getServer() + "api/topics.json";
		} else {
			return context.getServer() + "api/topics/node/" + nodeId + ".json";
		}
	}

	@Override
	public Class<ActiveTopicsResponse> getResponseClass() {
		return ActiveTopicsResponse.class;
	}

	@Override
	public Map<String, String> getTextParams(RCAPIContext context) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(SIZE_KEY, size + "");
		return params;
	}

	@Override
	public String getCacheRelativePathOrURL() {
		//NOTE always ensure providing a unique cache for a request
		return makeCachePath("api", "topics", "list", "default", size + "");
	}

	@Override
	public long getCacheTime() {
		return Cache.EXPIRED;
	}

}
