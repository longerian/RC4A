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
package org.rubychina.android.api.request;

import java.util.HashMap;
import java.util.Map;

import org.rubychina.android.api.RCAPIContext;
import org.rubychina.android.api.response.NodesResponse;

import yek.cache.Cache;

public class NodesRequest extends RCAPIGet<NodesResponse> {

	private static final String TAG = "NodesRequest";
	private static final String url = "api/nodes.json";
	
	@Override
	public String getRequestURL(RCAPIContext context) {
		return context.getServer() + url;
	}

	@Override
	public Class<NodesResponse> getResponseClass() {
		return NodesResponse.class;
	}

	@Override
	public Map<String, String> getTextParams(RCAPIContext context) {
		//NOTE because request for hot topics doesn't need any particular parameters, so just return a empty hashmap.
		HashMap<String, String> params = new HashMap<String, String>();
		return params;
	}

	@Override
	public String getCacheRelativePathOrURL() {
		//NOTE always ensure providing a unique cache for a request
		return makeCachePath("api", "nodes", "list");
	}

	@Override
	public long getCacheTime() {
		//Thought nodes do not change frequently, cache lasts for an hour.
		return Cache.EXPIRED;
	}

}
