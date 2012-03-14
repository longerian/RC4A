package org.rubychina.android;

import android.content.SharedPreferences;

public class Preferences {
	
    private static final String TAG = "Preferences";

    public static final String PREFERENCE_APP = "RubyChinaClient";
    
    public static void setupPreferences(SharedPreferences preferences) {
//    	Editor editor = preferences.edit();
//        if (!preferences.contains(USER_NAME)) {
//        	editor.putString(USER_NAME, "");
//        }
//        if (!preferences.contains(USER_PWD)) {
//        	editor.putString(USER_PWD, "");
//        }
//        if (!preferences.contains(APP_WEBLOGID)) {
//            editor.putString(APP_WEBLOGID, "");
//        }
//        if (!preferences.contains(SHOPCAR_NUM)) {
//            editor.putInt(SHOPCAR_NUM, 0);
//        }
//        if(!preferences.contains(SOURCE_ID)) {
//        	editor.putString(SOURCE_ID, "");
//        }
//        editor.commit();
    }
    
//    /**
//     * 设置weblogid到sharedpreference
//     * @param prefs
//     * @param weblogid
//     * @return
//     */
//    public static boolean setWeblogid(SharedPreferences prefs, Weblogid weblogid) {
//    	Editor editor = prefs.edit();
//      editor.putString(APP_WEBLOGID, weblogid.getWeblogid());
//      return editor.commit();
//    }
//
//    /**
//     * 通过用户名密码来判断用户有无登录
//     * @param prefs
//     * @return 是否有用户信息
//     */
//    public static boolean isLogin(SharedPreferences prefs) {
//    	return (prefs.getString(USER_NAME, null) != null )
//    		&& (prefs.getString(USER_NAME, null) != "" )
//    		&& (prefs.getString(USER_PWD, null) != null )
//    		&& (prefs.getString(USER_PWD, null) != "" );
//    }
//
//    /**
//     * 从sharedpreference读取weblogid
//     * @param prefs
//     * @return
//     */
//    public static String getWeblogId(SharedPreferences prefs) {
//    	return prefs.getString(APP_WEBLOGID, "");
//    }
//
//    public static boolean setUserName(SharedPreferences prefs, String name) {
//    	Editor editor = prefs.edit();
//    	editor.putString(USER_NAME, name);
//    	return editor.commit();
//    }
//    
//    public static String getUserName(SharedPreferences prefs) {
//    	return prefs.getString(USER_NAME, "");
//    }
//    
//    /**
//     * 获取商品来源id
//     * @param prefs
//     * @return
//     */
//    public static String getProductFromId(SharedPreferences fromIdprefs,String skuString) {
//    	Log.i("******", fromIdprefs.getString(skuString, "0") + "*"  + skuString);
//    	return fromIdprefs.getString(skuString, "0");
//    }
//    /**
//     * 设置商品的来源id
//     * @param prefs
//     * @return
//     */
//    public static boolean setProductFromId(SharedPreferences fromIdprefs, String fromId,String skuString) {
//    	Editor editor = fromIdprefs.edit();
//    	editor.putString(skuString, fromId);
//    	Log.i("******",fromId + " **** " +skuString) ;
//    	
//    	return editor.commit();
//    }
//    
//    public static boolean clearPoductFromIdData(SharedPreferences fromIdprefs){
//    	return fromIdprefs.edit().clear().commit();
//    	
//    }
//    
//    public static boolean setShopcarNum(SharedPreferences prefs, int shopcarnum) {
//    	Editor editor = prefs.edit();
//    	editor.putInt(SHOPCAR_NUM, shopcarnum);
//    	return editor.commit();
//    }
//    
//    public static int getShopcarNum(SharedPreferences prefs) {
//    	return prefs.getInt(SHOPCAR_NUM, 0);
//    }
//    
//    public static boolean setUserPwd(SharedPreferences prefs, String pwd) {
//    	Editor editor = prefs.edit();
//        editor.putString(USER_PWD, pwd);
//        return editor.commit();
//    }
//    
//	public static String getUserPwd(SharedPreferences prefs) {
//		return prefs.getString(USER_PWD, "");
//	}
//    
//	public static boolean setSourceId(SharedPreferences prefs, String sourceId) {
//    	Editor editor = prefs.edit();
//        editor.putString(SOURCE_ID, sourceId);
//        return editor.commit();
//    }
//    
//	public static String getSourceId(SharedPreferences prefs) {
//		return prefs.getString(SOURCE_ID, "");
//	}
	
}
