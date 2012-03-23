/*Copyright (C) 2010 Longerian (http://www.longerian.me)

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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rubychina.android.api.response.ActiveTopicsResponse;
import org.rubychina.android.type.Topic;

import com.google.gson.Gson;

public class ActiveTopicsParser extends JSONParseHandler<ActiveTopicsResponse> {

	private ActiveTopicsResponse resp = new ActiveTopicsResponse();
	
	@Override
	public ActiveTopicsResponse getModel() {
		return resp;
	}

	@Override
	public void parse(String source) {
		try {
			JSONArray jsonTopics = new JSONArray(source);
			List<Topic> topics = new ArrayList<Topic>();
			int length = jsonTopics.length();
			for(int i = 0; i < length; i++) {
				topics.add(json2Topic(jsonTopics.getJSONObject(i)));
			}
			resp.setTopics(topics);
			resp.setSuccess(true);
		} catch (JSONException e) {
			resp.setSuccess(false);
			e.printStackTrace();
		}
	}

	private Topic json2Topic(JSONObject json) {
		Gson gson = new Gson();
		Topic t = gson.fromJson(json.toString(), Topic.class);
		return t;
	}
	
}
