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
