package org.rubychina.android.api.request;

import org.apache.http.client.methods.HttpGet;
import org.rubychina.android.api.response.RCAPIResponse;

public abstract class RCAPIGet<R extends RCAPIResponse> extends RCAPIRequest<R> {

	@Override
	public Class<?> getHttpRequestClass() {
		return HttpGet.class;
	}

}
