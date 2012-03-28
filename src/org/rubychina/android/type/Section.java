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

public class Section implements Comparable<Section> {

	private int sectionId;
	
	private int sort;
	
	private String sectionName;

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
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
		result = prime * result + sectionId;
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
		Section other = (Section) obj;
		if (sectionId != other.sectionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return sectionName;
	}

	@Override
	public int compareTo(Section another) {
		return sectionId - another.sectionId;
	}
	
}
