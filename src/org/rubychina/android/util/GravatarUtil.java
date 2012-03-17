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
