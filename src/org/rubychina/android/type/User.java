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

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

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
	
	@SerializedName("topics")  
	private List<Topic> recentTopicCreated = new ArrayList<Topic>(); //最近发布帖子

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
	
	public List<Topic> getRecentTopicCreated() {
		return recentTopicCreated;
	}

	public User(Parcel in) {
		readFromParcel(in);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(login);
		dest.writeString(location);
		dest.writeString(website);
		dest.writeString(bio);
		dest.writeString(tagline);
		dest.writeString(githubUrl);
		dest.writeString(gravatarHash);
		dest.writeString(avatarUrl);
		dest.writeTypedList(recentTopicCreated);
	}
	
	@SuppressWarnings("unchecked")
	private void readFromParcel(Parcel in) {
		id = in.readInt();
		name = in.readString();
		login = in.readString();
		location = in.readString();
		website = in.readString();
		bio = in.readString();
		tagline = in.readString();
		githubUrl = in.readString();
		gravatarHash = in.readString();
		avatarUrl = in.readString();
		in.readTypedList(recentTopicCreated, CREATOR);
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = 
		new Parcelable.Creator() {
	
            public User createFromParcel(Parcel in) {
                return new User(in);
            }
 
            public User[] newArray(int size) {
                return new User[size];
            }
            
        };
	        
}
