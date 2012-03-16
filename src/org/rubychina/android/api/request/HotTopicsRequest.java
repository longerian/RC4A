package org.rubychina.android.api.request;

import java.util.HashMap;
import java.util.Map;

import org.rubychina.android.api.RCAPIContext;
import org.rubychina.android.api.response.HotTopicsResponse;

import yek.cache.Cache;

public class HotTopicsRequest extends RCAPIGet<HotTopicsResponse> {

	private static final String TAG = "HotTopicsRequest";
	private static final String url = "api/topics.json";
	
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
		//NOTE because request for hot topics doesn't need any particular parameters, so just return a empty hashmap.
		HashMap<String, String> params = new HashMap<String, String>();
		return params;
	}

	@Override
	public String getCacheRelativePathOrURL() {
		//NOTE always ensure providing a unique cache for a request
		return makeCachePath("api", "topics", "list", "default");
	}

	@Override
	public long getCacheTime() {
		return Cache.EXPIRED;
	}

}
