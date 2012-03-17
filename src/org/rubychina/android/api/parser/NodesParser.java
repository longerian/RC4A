package org.rubychina.android.api.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rubychina.android.api.response.NodesResponse;
import org.rubychina.android.type.Node;

import com.google.gson.Gson;

public class NodesParser extends JSONParseHandler<NodesResponse> {

	private NodesResponse resp = new NodesResponse();
	
	@Override
	public NodesResponse getModel() {
		return resp;
	}

	@Override
	public void parse(String source) {
		try {
			JSONArray jsonNodes = new JSONArray(source);
			List<Node> nodes = new ArrayList<Node>();
			int length = jsonNodes.length();
			for(int i = 0; i < length; i++) {
				nodes.add(json2Node(jsonNodes.getJSONObject(i)));
			}
			resp.setNodes(nodes);
			resp.setSuccess(true);
		} catch (JSONException e) {
			//TODO set meaningful failure msg
			resp.setSuccess(false);
			e.printStackTrace();
		}
	}

	private Node json2Node(JSONObject json) {
		Gson gson = new Gson();
		Node n = gson.fromJson(json.toString(), Node.class);
		return n;
	}
}
