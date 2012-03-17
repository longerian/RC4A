package org.rubychina.android;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {
	
    private static final String TAG = "Preferences";

    public static final String PREFERENCE_APP = "RubyChina";
    
    private static final String TOKEN = "token";
    
    public static void setupPreferences(SharedPreferences preferences) {
    	Editor editor = preferences.edit();
        if (!preferences.contains(TOKEN)) {
        	editor.putString(TOKEN, "");
        }
        editor.commit();
    }
    
    public static boolean setToken(SharedPreferences prefs, String token) {
    	Editor editor = prefs.edit();
    	editor.putString(TOKEN, token);
    	return editor.commit();
    }

    public static boolean getToken(SharedPreferences prefs) {
    	return (prefs.getString(TOKEN, null) != null );
    }
	
}
