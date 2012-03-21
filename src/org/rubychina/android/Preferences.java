package org.rubychina.android;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {
	
    private static final String TAG = "Preferences";

    public static final String PREFERENCE_APP = "RubyChina";
    
    private static final String TOKEN = "token";
    private static final String PAGE_SIZE = "page_size";
    
    private static final int DEFAULT_PAGE_SIZE = 30;
    
    public static void setupPreferences(SharedPreferences preferences) {
    	Editor editor = preferences.edit();
        if(!preferences.contains(TOKEN)) {
        	editor.putString(TOKEN, "");
        }
        if(!preferences.contains(PAGE_SIZE)) {
        	editor.putInt(PAGE_SIZE, DEFAULT_PAGE_SIZE);
        }
        editor.commit();
    }
    
    public static boolean setToken(SharedPreferences prefs, String token) {
    	Editor editor = prefs.edit();
    	editor.putString(TOKEN, token);
    	return editor.commit();
    }

    public static String getToken(SharedPreferences prefs) {
    	return prefs.getString(TOKEN, null);
    }
	
    public static boolean setPageSize(SharedPreferences prefs, int size) {
    	Editor editor = prefs.edit();
    	editor.putInt(PAGE_SIZE, size);
    	return editor.commit();
    }
    
    public static int getPageSize(SharedPreferences prefs) {
    	return prefs.getInt(PAGE_SIZE, DEFAULT_PAGE_SIZE);
    }
    
}
