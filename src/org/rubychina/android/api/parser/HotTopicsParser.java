package org.rubychina.android.api.parser;

import org.rubychina.android.api.response.HotTopicsResponse;
import org.rubychina.android.util.LogUtil;

public class HotTopicsParser extends JSONParseHandler<HotTopicsResponse> {

	private HotTopicsResponse resp = new HotTopicsResponse();
	
	@Override
	public HotTopicsResponse getModel() {
		return resp;
	}

	@Override
	public void parse(String source) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG, source);
	}

}
