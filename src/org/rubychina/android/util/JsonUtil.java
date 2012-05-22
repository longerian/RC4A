package org.rubychina.android.util;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;

public class JsonUtil {

	private static final Gson G = new Gson(); 
	
	private JsonUtil() {
		
	}
	
	public static <T> T fromJsonObject(String json, Class<T> classOfT) {
		return G.fromJson(json, classOfT);
	}
	
	public static <T> String toJsonObject(T t) {
		return G.toJson(t);
	}
	
	public static <T> List<T> fromJsonArray(String json, Type type) {
		return G.fromJson(json, type);
	}
	
	public static <T> String toJsonArray(List<T> list, Type type) {
		return G.toJson(list, type);
	}
	
}
