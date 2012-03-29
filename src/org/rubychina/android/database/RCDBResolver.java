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
import org.rubychina.android.type.Topic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public enum RCDBResolver {

	INSTANCE;
	
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
		Gson g = new Gson();
		Type type = new TypeToken<List<Topic>>(){}.getType();
		SQLiteDatabase db = sHelper.getWritableDatabase();
		try {
			values.put(SQLiteHelper.CLM_TOPIC, g.toJson(topics, type));
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
				Gson g = new Gson();
				Type type = new TypeToken<List<Topic>>(){}.getType();
				while(!cursor.isAfterLast()) {
					ts = g.fromJson(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.CLM_TOPIC)), 
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
		Gson g = new Gson();
		Type type = new TypeToken<List<Node>>(){}.getType();
		SQLiteDatabase db = sHelper.getWritableDatabase();
		try {
			values.put(SQLiteHelper.CLM_NODE, g.toJson(nodes, type));
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
				Gson g = new Gson();
				Type type = new TypeToken<List<Node>>(){}.getType();
				while(!cursor.isAfterLast()) {
					 ns = g.fromJson(
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

	public class SQLiteHelper extends SQLiteOpenHelper {

		private static final String DATABASE_NAME = "RubyChina.db";
		private static final int DATABASE_VERSION = 5;
		
		public static final String TBL_TOPIC = "topics";
		public static final String CLM_TOPIC = "topic";
		
		public static final String TBL_NODE = "nodes";
		public static final String CLM_NODE = "node";
		
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
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists " + TBL_TOPIC);
			db.execSQL("drop table if exists " + TBL_NODE);
		    onCreate(db);
		}
		
	}

}
