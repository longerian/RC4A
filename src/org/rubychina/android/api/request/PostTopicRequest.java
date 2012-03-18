package org.rubychina.android.api.request;

import java.util.HashMap;
import java.util.Map;

import org.rubychina.android.api.RCAPIContext;
import org.rubychina.android.api.response.PostTopicResponse;

import yek.cache.Cache;

public class PostTopicRequest extends RCAPIPost<PostTopicResponse> {

	private static final String TAG = "PostTopicRequest";
	private static final String url = "api/topics.json";
	
	private static final String TOKEN_KEY = "token";
	private static final String TITLE_KEY = "title";
	private static final String BODY_KEY = "body";
	private static final String NODE_ID_KEY = "node_id";
	
	@Override
	public String getRequestURL(RCAPIContext context) {
		return context.getServer() + url;
	}

	@Override
	public Class<PostTopicResponse> getResponseClass() {
		return PostTopicResponse.class;
	}

	@Override
	public Map<String, String> getTextParams(RCAPIContext context) {
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put(TITLE_KEY, "android client test");
		params.put(BODY_KEY, "att");
		params.put(NODE_ID_KEY, "27");
		params.put(TOKEN_KEY, "4e3cec5794bb58a009c9");
		return params;
	}

	@Override
	public String getCacheRelativePathOrURL() {
		//NOTE always ensure providing a unique cache for a request
		return makeCachePath("api", "topics", "post", "default");
	}

	@Override
	public long getCacheTime() {
		return Cache.EXPIRED;
	}

}
