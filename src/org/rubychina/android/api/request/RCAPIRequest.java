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
