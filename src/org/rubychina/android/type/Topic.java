package org.rubychina.android.type;

import java.util.Date;

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
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getBodyHTML() {
		return bodyHTML;
	}
	public void setBodyHTML(String bodyHTML) {
		this.bodyHTML = bodyHTML;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getRepliedAt() {
		return repliedAt;
	}
	public void setRepliedAt(String repliedAt) {
		this.repliedAt = repliedAt;
	}
	public int getRepliesCount() {
		return repliesCount;
	}
	public void setRepliesCount(int repliesCount) {
		this.repliesCount = repliesCount;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public int getNodeID() {
		return nodeID;
	}
	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}
	public String getLastReplyUserLogin() {
		return lastReplyUserLogin;
	}
	public void setLastReplyUserLogin(String lastReplyUserLogin) {
		this.lastReplyUserLogin = lastReplyUserLogin;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return "Topic [title=" + title + "]";
	}
	
}
