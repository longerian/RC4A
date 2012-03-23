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

import org.rubychina.android.api.RCAPIContext;
import org.rubychina.android.api.response.HotTopicsResponse;

import yek.cache.Cache;

public class HotTopicsRequest extends RCAPIGet<HotTopicsResponse> {

	private static final String TAG = "HotTopicsRequest";
	private static final String url = "api/topics.json";
	
	private static final int DEFAULT_SIZE = 30;
	
	private static final String SIZE_KEY = "size";
//	private static final String NODE_ID_KEY = "size";
	
	private int size = DEFAULT_SIZE;
	private int nodeId;
	
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
		return context.getServer() + url;
	}

	@Override
	public Class<HotTopicsResponse> getResponseClass() {
		return HotTopicsResponse.class;
	}

	@Override
	public Map<String, String> getTextParams(RCAPIContext context) {
		//TODO add node id parameter
		//TODO the size seems do not work
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
