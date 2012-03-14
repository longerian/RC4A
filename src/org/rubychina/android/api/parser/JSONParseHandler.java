package org.rubychina.android.api.parser;

import yek.api.parser.ParseHandler;

public abstract class JSONParseHandler<T> implements ParseHandler<T> {

	protected static final String TAG = "JSONParseHandler";
	
	public abstract void parse(String source);

}
