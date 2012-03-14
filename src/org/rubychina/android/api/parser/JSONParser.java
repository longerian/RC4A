package org.rubychina.android.api.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import yek.api.parser.ParseException;
import yek.api.parser.Parser;
import yek.util.Util;

public class JSONParser implements Parser {

	private JSONParserHandlerFactory mHandlerFactory;
	
	public JSONParser() {
		mHandlerFactory = new JSONParserHandlerFactory();
	}
	
	@Override
	public <T> T parse(String str, Class<T> clazz) throws ParseException {
		InputStream is = new ByteArrayInputStream(str.getBytes());
		return parse(is, clazz);
	}

	@Override
	public <T> T parse(File file, Class<T> clazz) throws ParseException {
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new ParseException();
		}
		return parse(input, clazz);
	}

	@Override
	public <T> T parse(InputStream is, Class<T> clazz) throws ParseException {
		return getModelFromXML(is, clazz);
	}
	
	private <T> T getModelFromXML(InputStream inputSource, Class<T> clazz) {
		JSONParseHandler<T> handler = (JSONParseHandler<T>) mHandlerFactory.newHandler(clazz);
		String source;
		try {
			source = Util.inputStreamToString(inputSource);
			handler.parse(source);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return handler.getModel();
	}

}
