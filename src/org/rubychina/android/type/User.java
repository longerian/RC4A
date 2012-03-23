/*Copyright (C) 2012 Longerian (http://www.longerian.me)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
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
