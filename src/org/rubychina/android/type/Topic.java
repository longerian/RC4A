package org.rubychina.android.type;

import com.google.gson.annotations.SerializedName;

public class Topic extends RCType {
	
	@SerializedName("_id")  
	private int id;
	
	private String title;
	
	private String body;
	
	@SerializedName("body_html")  
	private String bodyHTML;
	
	@SerializedName("created_at")  
	private String createdAt;
	
	@SerializedName("updated_at")  
	private String updatedAt;
	
	@SerializedName("replied_at")  
	private String repliedAt;
	
	@SerializedName("replies_count")  
	private int repliesCount;
	
	@SerializedName("node_name")  
	private String nodeName;
	
	@SerializedName("node_id")  
	private int nodeID;
	
	@SerializedName("last_reply_user_login")  
	private String lastReplyUserLogin;
	
	private User user;

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public String getBodyHTML() {
		return bodyHTML;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public String getRepliedAt() {
		return repliedAt;
	}

	public int getRepliesCount() {
		return repliesCount;
	}

	public String getNodeName() {
		return nodeName;
	}

	public int getNodeID() {
		return nodeID;
	}

	public String getLastReplyUserLogin() {
		return lastReplyUserLogin;
	}

	public User getUser() {
		return user;
	}
	
}
