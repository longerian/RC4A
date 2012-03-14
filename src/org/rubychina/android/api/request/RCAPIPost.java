package org.rubychina.android.api.request;

import org.apache.http.client.methods.HttpPost;
import org.rubychina.android.api.response.RCAPIResponse;

public abstract class RCAPIPost<R extends RCAPIResponse> extends RCAPIRequest<R> {

	@Override
	public Class<?> getHttpRequestClass() {
		return HttpPost.class;
	}

}
