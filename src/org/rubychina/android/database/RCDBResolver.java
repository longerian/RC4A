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
package org.rubychina.android.database;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.rubychina.android.type.Node;
import org.rubychina.android.type.SiteGroup;
import org.rubychina.android.type.Topic;
import org.rubychina.android.type.User;
import org.rubychina.android.util.JsonUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.google.gson.reflect.TypeToken;

public enum RCDBResolver {

	INSTANCE;
	
	private static final String TAG = "RCDBResolver";
	private SQLiteHelper usHelper;
	
	private synchronized SQLiteHelper sHelperFromUs(Context ctx) {
		if(usHelper == null) {
			usHelper = new SQLiteHelper(ctx, SQLiteHelper.DATABASE_NAME, null, SQLiteHelper.DATABASE_VERSION);
		}
		return usHelper;
	}
	
	public synchronized boolean clearTopics(Context context) {
		SQLiteHelper sHelper = sHelperFromUs(context);
		SQLiteDatabase db = sHelper.getWritableDatabase();
		try {
			db.delete(SQLiteHelper.TBL_TOPIC, null, null);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
		return true;
	}

	public synchronized boolean insertTopics(Context context, List<Topic> topics) {
		SQLiteHelper sHelper = sHelperFromUs(context);
		ContentValues values = new ContentValues();
		SQLiteDatabase db = sHelper.getWritableDatabase();
		try {
			Type type = new TypeToken<List<Topic>>(){}.getType();
			values.put(SQLiteHelper.CLM_TOPIC, JsonUtil.toJsonArray(topics, type));
			db.insert(SQLiteHelper.TBL_TOPIC, null, values);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
		return true;
	}
	
	public synchronized List<Topic> fetchTopics(Context context) {
		SQLiteHelper sHelper = sHelperFromUs(context);
		SQLiteDatabase db = sHelper.getWritableDatabase();
		Cursor cursor = null;
		List<Topic> ts = new ArrayList<Topic>();
		try {
			cursor = db.query(SQLiteHelper.TBL_TOPIC, null, null, null, null, null, null);
			if(cursor != null) {
				cursor.moveToFirst();
				Type type = new TypeToken<List<Topic>>(){}.getType();
				while(!cursor.isAfterLast()) {
					ts = JsonUtil.fromJsonArray(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.CLM_TOPIC)), 
							type);
					cursor.moveToNext();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return ts;
	}
	
	public synchronized boolean clearNodes(Context context) {
		SQLiteHelper sHelper = sHelperFromUs(context);
		SQLiteDatabase db = sHelper.getWritableDatabase();
		try {
			db.delete(SQLiteHelper.TBL_NODE, null, null);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
		return true;
	}
	
	public synchronized boolean insertNodes(Context context, List<Node> nodes) {
		SQLiteHelper sHelper = sHelperFromUs(context);
		ContentValues values = new ContentValues();
		SQLiteDatabase db = sHelper.getWritableDatabase();
		try {
			Type type = new TypeToken<List<Node>>(){}.getType();
			String s = JsonUtil.toJsonArray(nodes, type);
			values.put(SQLiteHelper.CLM_NODE, s);
			db.insert(SQLiteHelper.TBL_NODE, null, values);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
		return true;
	}
	
	public synchronized List<Node> fetchNodes(Context context) {
		SQLiteHelper sHelper = sHelperFromUs(context);
		SQLiteDatabase db = sHelper.getWritableDatabase();
		Cursor cursor = null;
		List<Node> ns = new ArrayList<Node>();
		try {
			cursor = db.query(SQLiteHelper.TBL_NODE, null, null, null, null, null, null);
			if(cursor != null) {
				cursor.moveToFirst();
				Type type = new TypeToken<List<Node>>(){}.getType();
				while(!cursor.isAfterLast()) {
					 ns = JsonUtil.fromJsonArray(
							cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.CLM_NODE)), 
							type);
					cursor.moveToNext();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return ns;
	}
	
	public synchronized boolean clearUsers(Context context) {
		SQLiteHelper sHelper = sHelperFromUs(context);
		SQLiteDatabase db = sHelper.getWritableDatabase();
		try {
			db.delete(SQLiteHelper.TBL_USER, null, null);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
		return true;
	}
	
	public synchronized boolean insertUsers(Context context, List<User> users) {
		SQLiteHelper sHelper = sHelperFromUs(context);
		ContentValues values = new ContentValues();
		SQLiteDatabase db = sHelper.getWritableDatabase();
		try {
			Type type = new TypeToken<List<User>>(){}.getType();
			String s = JsonUtil.toJsonArray(users, type);
			values.put(SQLiteHelper.CLM_USER, s);
			db.insert(SQLiteHelper.TBL_USER, null, values);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
		return true;
	}
	
	public synchronized List<User> fetchUsers(Context context) {
		SQLiteHelper sHelper = sHelperFromUs(context);
		SQLiteDatabase db = sHelper.getWritableDatabase();
		Cursor cursor = null;
		List<User> us = new ArrayList<User>();
		try {
			cursor = db.query(SQLiteHelper.TBL_USER, null, null, null, null, null, null);
			if(cursor != null) {
				cursor.moveToFirst();
				Type type = new TypeToken<List<User>>(){}.getType();
				while(!cursor.isAfterLast()) {
					 us = JsonUtil.fromJsonArray(
							cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.CLM_USER)), 
							type);
					cursor.moveToNext();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return us;
	}
	
	public synchronized boolean clearSites(Context context) {
		SQLiteHelper sHelper = sHelperFromUs(context);
		SQLiteDatabase db = sHelper.getWritableDatabase();
		try {
			db.delete(SQLiteHelper.TBL_SITE, null, null);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
		return true;
	}
	
	public synchronized boolean insertSites(Context context, List<SiteGroup> siteGroup) {
		SQLiteHelper sHelper = sHelperFromUs(context);
		ContentValues values = new ContentValues();
		SQLiteDatabase db = sHelper.getWritableDatabase();
		try {
			Type type = new TypeToken<List<SiteGroup>>(){}.getType();
			String s = JsonUtil.toJsonArray(siteGroup, type);
			values.put(SQLiteHelper.CLM_SITE, s);
			db.insert(SQLiteHelper.TBL_SITE, null, values);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
		return true;
	}

	public synchronized List<SiteGroup> fetchSites(Context context) {
		SQLiteHelper sHelper = sHelperFromUs(context);
		SQLiteDatabase db = sHelper.getWritableDatabase();
		Cursor cursor = null;
		List<SiteGroup> sgs = new ArrayList<SiteGroup>();
		try {
			cursor = db.query(SQLiteHelper.TBL_SITE, null, null, null, null, null, null);
			if(cursor != null) {
				cursor.moveToFirst();
				Type type = new TypeToken<List<SiteGroup>>(){}.getType();
				while(!cursor.isAfterLast()) {
					 sgs = JsonUtil.fromJsonArray(
							cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.CLM_SITE)), 
							type);
					cursor.moveToNext();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return sgs;
	}
	
	public class SQLiteHelper extends SQLiteOpenHelper {

		private static final String DATABASE_NAME = "RubyChina.db";
		private static final int DATABASE_VERSION = 7;
		
		public static final String TBL_TOPIC = "topics";
		public static final String CLM_TOPIC = "topic";
		
		public static final String TBL_NODE = "nodes";
		public static final String CLM_NODE = "node";
		
		public static final String TBL_SITE = "sites";
		public static final String CLM_SITE = "site";
		
		public static final String TBL_USER = "users";
		public static final String CLM_USER = "user";
		
		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table if not exists " + TBL_TOPIC
					+ " (" + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
					+ CLM_TOPIC + " TEXT NOT NULL"
					+ ");");
			db.execSQL("create table if not exists " + TBL_NODE
					+ " (" + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
					+ CLM_NODE + " TEXT NOT NULL"
					+ ");");
			db.execSQL("create table if not exists " + TBL_SITE
					+ " (" + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
					+ CLM_SITE + " TEXT NOT NULL"
					+ ");");
			db.execSQL("create table if not exists " + TBL_USER
					+ " (" + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
					+ CLM_USER + " TEXT NOT NULL"
					+ ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists " + TBL_TOPIC);
			db.execSQL("drop table if exists " + TBL_NODE);
			db.execSQL("drop table if exists " + TBL_SITE);
			db.execSQL("drop table if exists " + TBL_USER);
		    onCreate(db);
		}
		
	}

}
