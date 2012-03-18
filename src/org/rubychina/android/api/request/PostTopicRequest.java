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
	
	private String title;
	private String body;
	private String nodeId;
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

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
		params.put(TITLE_KEY, title);
		params.put(BODY_KEY, body);
		params.put(NODE_ID_KEY, nodeId);
		params.put(TOKEN_KEY, context.getToken());
		return params;
	}

	@Override
	public String getCacheRelativePathOrURL() {
		//NOTE always ensure providing a unique cache for a request
		return makeCachePath("api", "topics", "post", System.currentTimeMillis() / 1000 + "");
	}

	@Override
	public long getCacheTime() {
		return Cache.EXPIRED;
	}

}
