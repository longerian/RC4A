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
