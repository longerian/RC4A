package org.rubychina.android.type;

import com.google.gson.annotations.SerializedName;

public class Reply {

	@SerializedName("_id")  
	private int id;
	
	private String body;;
	
	@SerializedName("body_html")  
	private String bodyHTML;
	
	@SerializedName("message_id")  
	private String messageID;
	
	@SerializedName("created_at")  
	private String createdAt;
	
	@SerializedName("updated_at")  
	private String updatedAt;
	
	private User user;

	public int getId() {
		return id;
	}

	public String getBody() {
		return body;
	}

	public String getBodyHTML() {
		return bodyHTML;
	}

	public String getMessageID() {
		return messageID;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public User getUser() {
		return user;
	}
	
}
