package org.rubychina.android.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.rubychina.android.RCApplication;
import org.rubychina.android.api.request.RCAPIRequest;
import org.rubychina.android.api.response.RCAPIResponse;
import org.rubychina.android.util.LogUtil;

import yek.api.ApiClient;
import yek.api.parser.Parser;
import yek.cache.Cache;
import android.content.Context;

public class RCAPIClient extends ApiClient<RCAPIContext, RCAPIResponse, RCAPIRequest<? extends RCAPIResponse>> 
	implements RCAPIConstant, RCAPIContext {

	private static final String TAG = "RCAPIClient";
	
	private Context mContext;
	
	public RCAPIClient(Context context, Parser parser,
			ThreadPoolExecutor threadPool, Cache cache) {
		super(context, parser, threadPool, cache, new BasicCookieStore());
		mContext = context;
	}
	
	@Override
	protected Header[] makeRequestHeaders(
			RCAPIRequest<? extends RCAPIResponse> request) {
		//NOTE: if there's public headers for this api request, then initialize them here. Otherwise just leave it empty like code below.
		HttpPost httpPost = new HttpPost();
		return httpPost.getAllHeaders();
	}

	@Override
	protected Map<String, String> makeRequestParam(
			RCAPIRequest<? extends RCAPIResponse> request) {
		Map<String, String> param = new HashMap<String, String>();
		param.putAll(request.getTextParams(getApiContext()));
		return param;
	}

	@Override
	public Context getContext() {
		return mContext;
	}

	@Override
	public String getParam(String arg0) {
		// NOTE this method is not used
		return null;
	}

	@Override
	public String getServer() {
		return SERVER;
	}
	
	@Override
	public String getToken() {
		return ((RCApplication) mContext.getApplicationContext()).getToken();
	}

	@Override
	public RCAPIContext getApiContext() {
		return this;
	}

}
