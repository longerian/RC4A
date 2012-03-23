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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.google.gson.annotations.SerializedName;

public class Reply implements Comparable<Reply> {

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

	@Override
	public int compareTo(Reply another) {
		//2012-03-14T09:28:39+08:00
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
		try {
			sdf.parse(this.createdAt);
			long one = sdf.getCalendar().getTimeInMillis() / 1000;
			
			sdf.parse(another.createdAt);
			long theOther = sdf.getCalendar().getTimeInMillis() / 1000;
			
			int diff = (int) (one - theOther);
			return diff;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
