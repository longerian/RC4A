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
