package org.rubychina.android.type;

import com.google.gson.annotations.SerializedName;

public class User {

	@SerializedName("_id")  
	private int id;
	
	private String name; //昵称
	
	private String login; //登录id
	
	private String location;
	
	private String website;
	
	private String bio; //个人介绍
	
	private String tagline; //签名
	
	@SerializedName("github_url")  
	private String githubUrl;
	
	@SerializedName("gravatar_hash")  
	private String gravatarHash;
	
	@SerializedName("avatar_url")  
	private String avatarUrl;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLogin() {
		return login;
	}

	public String getLocation() {
		return location;
	}

	public String getWebsite() {
		return website;
	}

	public String getBio() {
		return bio;
	}

	public String getTagline() {
		return tagline;
	}

	public String getGithubUrl() {
		return githubUrl;
	}

	public String getGravatarHash() {
		return gravatarHash;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}
	
}
