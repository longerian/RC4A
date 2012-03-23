/*Copyright (C) 2010 Longerian (http://www.longerian.me)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package org.rubychina.android.util;

/**
 * @author Longer
 * return gravatar urls, for more info about how the url works, visit http://en.gravatar.com/site/implement/images/
 */
public class GravatarUtil {

	private static final String BASE_URL = "http://www.gravatar.com/avatar/";
	
	public static String getBaseURL(String hash) {
		StringBuilder builder = new StringBuilder(BASE_URL);
		return builder.append(hash).toString();
	}
	
	public static String getURLWithSize(String hash, int size) {
		if(size < 1 || size > 512) {
			return getBaseURL(hash);
		} else {
			StringBuilder builder = new StringBuilder(BASE_URL);
			return builder.append(hash).append("?s=").append(size).toString();
		}
	}
}
