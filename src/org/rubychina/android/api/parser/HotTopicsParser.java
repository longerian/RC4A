package org.rubychina.android.api.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rubychina.android.api.response.HotTopicsResponse;
import org.rubychina.android.type.Topic;

import com.google.gson.Gson;

public class HotTopicsParser extends JSONParseHandler<HotTopicsResponse> {

	private HotTopicsResponse resp = new HotTopicsResponse();
	
	@Override
	public HotTopicsResponse getModel() {
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
