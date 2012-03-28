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
