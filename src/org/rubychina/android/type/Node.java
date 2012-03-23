/*Copyright (C) 2010 Longerian (http://www.longerian.me)

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

public class Node implements Parcelable {

	@SerializedName("_id")
	private int id;
	
	private String name;

	public Node() {
		super();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
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
	}
	
	private void readFromParcel(Parcel in) {
		id = in.readInt();
		name = in.readString();
	}
	
	public static final Parcelable.Creator CREATOR = 
			new Parcelable.Creator() {
		
	            public Node createFromParcel(Parcel in) {
	                return new Node(in);
	            }
	 
	            public Node[] newArray(int size) {
	                return new Node[size];
	            }
	            
	        };
	
}
