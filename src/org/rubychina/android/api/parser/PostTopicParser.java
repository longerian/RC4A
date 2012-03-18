package org.rubychina.android.api.parser;

import org.rubychina.android.api.response.PostTopicResponse;
import org.rubychina.android.util.LogUtil;

public class PostTopicParser extends JSONParseHandler<PostTopicResponse> {

	PostTopicResponse resp = new PostTopicResponse();
	
	@Override
	public PostTopicResponse getModel() {
		return resp;
	}

	@Override
	public void parse(String source) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG, source);
	}

}
