package org.rubychina.android.util;

import android.util.Log;

public class LogUtil {

	private static final boolean IS_DEBUG = true;
	
	public static void d(String tag, String msg) {
		if(IS_DEBUG) {
			Log.d(tag, msg);
		}
	}
	
}
