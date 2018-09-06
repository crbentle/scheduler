package com.bentley.scheduler.model;

import java.util.List;

public class Location {

	private String name;
	List<String> requiredQualifications;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getRequiredQualifications() {
		return requiredQualifications;
	}

	public void setRequiredQualifications(List<String> requiredQualifications) {
		this.requiredQualifications = requiredQualifications;
	}

	@Override
	public String toString() {
		return "Location [name=" + name + ", requiredQualifications=" + requiredQualifications + "]";
	}

}
