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
