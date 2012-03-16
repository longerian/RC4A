package org.rubychina.android.type;

import com.google.gson.annotations.SerializedName;

public class User extends RCType {

	@SerializedName("_id")  
	private int id;
	private String name;
	private String login; //TODO ??
	private String location;
	private String website;
	private String bio; //TODO ??
	private String tagline; //TODO ??
	@SerializedName("github_url")  
	private String githubUrl;
	@SerializedName("gravatar_hash")  
	private String gravatarHash;
	@SerializedName("avatar_url")  
	private String avatarUrl;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getTagline() {
		return tagline;
	}
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}
	public String getGithubUrl() {
		return githubUrl;
	}
	public void setGithubUrl(String githubUrl) {
		this.githubUrl = githubUrl;
	}
	public String getGravatarHash() {
		return gravatarHash;
	}
	public void setGravatarHash(String gravatarHash) {
		this.gravatarHash = gravatarHash;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	
}
