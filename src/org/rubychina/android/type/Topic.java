package org.rubychina.android.type;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Topic extends RCType {
	
	@SerializedName("_id")  
	private int id;
	private String title;
	private String body;
	private String bodyHTML;
	private Date createdAt;
	private Date updatedAt;
	private Date repliedAt;
	private Date repliesCount;
	private String nodeName;
	private int nodeID;
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
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Date getRepliedAt() {
		return repliedAt;
	}
	public void setRepliedAt(Date repliedAt) {
		this.repliedAt = repliedAt;
	}
	public Date getRepliesCount() {
		return repliesCount;
	}
	public void setRepliesCount(Date repliesCount) {
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
