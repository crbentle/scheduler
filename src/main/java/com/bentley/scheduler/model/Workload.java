package com.bentley.scheduler.model;

import java.util.HashMap;
import java.util.Map;

public class Workload {

	// staff map is Staff Type/number of workers
	private Map<String, Integer> requiredStaff = new HashMap<String, Integer>();
	private Map<String, Integer> recommendedStaff = new HashMap<String, Integer>();

	public Map<String, Integer> getRequiredStaff() {
		return requiredStaff;
	}

	public void setRequiredStaff(Map<String, Integer> requiredStaff) {
		this.requiredStaff = requiredStaff;
	}

	public Map<String, Integer> getRecommendedStaff() {
		return recommendedStaff;
	}

	public void setRecommendedStaff(Map<String, Integer> recommendedStaff) {
		this.recommendedStaff = recommendedStaff;
	}

	@Override
	public String toString() {
		return "Workload [requiredStaff=" + requiredStaff + ", recommendedStaff=" + recommendedStaff + "]";
	}


}
