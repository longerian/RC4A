package org.rubychina.android.api.request;

import org.rubychina.android.api.RCAPIConstant;
import org.rubychina.android.api.RCAPIContext;
import org.rubychina.android.api.response.RCAPIResponse;

import yek.api.ApiRequest;
import yek.util.FileUtil;
import yek.util.Util;

public abstract class RCAPIRequest<R extends RCAPIResponse> 
	implements ApiRequest<R, RCAPIContext>, RCAPIConstant {

	public static String makeCachePath(String ...strings){
		int len = strings.length;
		for(int i = 0; i < len; i ++) {
			if(Util.isEmpty(strings[i]))
				strings[i] = "empty";
		}
		return FileUtil.joinPath(strings);		
	}
	
}
