package org.rubychina.android.api.parser;

import yek.api.parser.ParseHandler;
import yek.api.parser.ParseHandlerFactory;

public class JSONParserHandlerFactory implements ParseHandlerFactory {

	@Override
	public <T> ParseHandler<T> newHandler(Class<T> clazz) {
		String response = clazz.getName();
		//NOTE here use Response's complete class name to looking for it's associated parser
		String handler = response.replace("Response", "Parser").replace("response", "parser");
		try {
			ParseHandler<T> h = (ParseHandler<T>)Class.forName(handler).newInstance();
			return h;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
