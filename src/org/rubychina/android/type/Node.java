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
