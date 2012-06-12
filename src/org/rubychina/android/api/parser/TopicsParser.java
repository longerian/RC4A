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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.rubychina.android.api.response.TopicsResponse;
import org.rubychina.android.type.Topic;
import org.rubychina.android.util.JsonUtil;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class TopicsParser extends JSONParseHandler<TopicsResponse> {

	private TopicsResponse resp = new TopicsResponse();
	
	@Override
	public TopicsResponse getModel() {
		return resp;
	}

	@Override
	public void parse(String source) {
		try {
			List<Topic> topics = new ArrayList<Topic>();
			Type type = new TypeToken<List<Topic>>(){}.getType();
			topics = JsonUtil.fromJsonArray(source, type);
			resp.setTopics(topics);
			resp.setSuccess(true);
		} catch (JsonSyntaxException e) {
			resp.setSuccess(false);
			e.printStackTrace();
		} catch (JsonParseException e) {
			resp.setSuccess(false);
			e.printStackTrace();
		} catch (NumberFormatException e) {
			resp.setSuccess(false);
			e.printStackTrace();
		}
	}

}
