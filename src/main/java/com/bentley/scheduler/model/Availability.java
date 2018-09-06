package com.bentley.scheduler.model;

import java.time.LocalDateTime;

public class Availability {

	// LocalDateTime.of(2015, Month.FEBRUARY, 20, 06, 30);
	LocalDateTime startTime;
	LocalDateTime endTime;
	
	public Availability( LocalDateTime startTime, LocalDateTime endTime ) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "[" + startTime + " - " + endTime + "]";
	}

}
