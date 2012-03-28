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
package org.rubychina.android.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {

	private HtmlUtil() {
		
	}
	
	public static boolean existsImg(String html) {
		Pattern pattern = Pattern.compile("<img.*>", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(html);
		while(matcher.find()) {
			return true;
		}
		return false;
	}
	
}
