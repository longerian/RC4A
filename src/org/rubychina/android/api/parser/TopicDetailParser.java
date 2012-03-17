package org.rubychina.android.api.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rubychina.android.api.response.TopicDetailResponse;
import org.rubychina.android.type.Reply;

import android.util.Log;

import com.google.gson.Gson;

public class TopicDetailParser extends JSONParseHandler<TopicDetailResponse> {

	private TopicDetailResponse resp = new TopicDetailResponse();
	
	@Override
	public TopicDetailResponse getModel() {
		return resp;
	}
	
	@Override
	public void parse(String source) {
		try {
			JSONArray jsonReplies = new JSONObject(source).getJSONArray("replies");
			List<Reply> replies = new ArrayList<Reply>();
			int length = jsonReplies.length();
			for(int i = 0; i < length; i++) {
				replies.add(json2Reply(jsonReplies.getJSONObject(i)));
			}
			resp.setReplies(replies);
			resp.setSuccess(true);
		} catch (JSONException e) {
			resp.setSuccess(false);
			e.printStackTrace();
		}
	}
	
	private Reply json2Reply(JSONObject json) {
		Gson gson = new Gson();
		Reply r = gson.fromJson(json.toString(), Reply.class);
		return r;
	}

}
