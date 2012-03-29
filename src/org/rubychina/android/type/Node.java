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

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Node implements Parcelable, Comparable<Node> {

	@SerializedName("_id")
	private int id;
	
	private String name;
	
	@SerializedName("topics_count")
	private int topicsCount;
	
	private String summary;
	
	@SerializedName("section_id")
	private int sectionId;
	
	private int sort;
	
	@SerializedName("section_name")
	private String sectionName;
	
	public Node() {
		super();
	}

	public Node(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Node(int id, String name, int sectionId, String sectionName) {
		super();
		this.id = id;
		this.name = name;
		this.sectionId = sectionId;
		this.sectionName = sectionName;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getTopicsCount() {
		return topicsCount;
	}

	public String getSummary() {
		return summary;
	}

	public int getSectionId() {
		return sectionId;
	}

	public int getSort() {
		return sort;
	}

	public String getSectionName() {
		return sectionName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public Section whichSection() {
		Section s = new Section();
		s.setSectionId(sectionId);
		s.setSort(sort);
		s.setSectionName(sectionName);
		return s;
	}

	public Node(Parcel in) {
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
		dest.writeInt(topicsCount);
		dest.writeString(summary);
		dest.writeInt(sectionId);
		dest.writeInt(sort);
		dest.writeString(sectionName);
	}
	
	private void readFromParcel(Parcel in) {
		id = in.readInt();
		name = in.readString();
		topicsCount = in.readInt();
		summary = in.readString();
		sectionId = in.readInt();
		sort = in.readInt();
		sectionName = in.readString();
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = 
			new Parcelable.Creator() {
		
	            public Node createFromParcel(Parcel in) {
	                return new Node(in);
	            }
	 
	            public Node[] newArray(int size) {
	                return new Node[size];
	            }
	            
	        };

	@Override
	public int compareTo(Node other) {
		int sectionIdDiff = sectionId - other.sectionId;
		if(sectionIdDiff != 0) {
			return sectionIdDiff;
		}
		return id - other.id;
	}
	
	public static final int ACTIVE_TOPICS_NODE_ID = -1;
	public static final String ACTIVE_TOPICS_NODE_NAME = "热门话题";
	public static final int ACTIVE_TOPICS_NODE_SECTION_ID = -1;
	public static final String ACTIVE_TOPICS_NODE_SECTION_NAME = "热门话题";
	
	public static final Node MOCK_ACTIVE_NODE = new Node(ACTIVE_TOPICS_NODE_ID, ACTIVE_TOPICS_NODE_NAME,
			ACTIVE_TOPICS_NODE_SECTION_ID, ACTIVE_TOPICS_NODE_SECTION_NAME);
	
}
