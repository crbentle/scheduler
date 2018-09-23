package com.bentley.scheduler.model;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Single instance of Clinic/continuity
 * @author Chris
 *
 */
public class Task {
	
	public Task(String name) {
		this.name = name;
	}
	
	String name;
	private Workload workload;
//	private List<Staff> roster;
	Map<Staff, Timeslot> roster;
	List<String> requiredQualifications;
	Location location;
	Timeslot schedule;

	// clinic details
	// 
	// Schedule - day/hours
	// recurring - daily, weekly (need to pick and choose days?)
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Workload getWorkload() {
		return workload;
	}

	public void setWorkload(Workload workload) {
		this.workload = workload;
	}

	public Map<Staff, Timeslot> getRoster() {
		return roster;
	}

	public void setRoster(Map<Staff, Timeslot> roster) {
		this.roster = roster;
	}
	
	public void addStaff( Staff staff, Timeslot availability ) {
		if( roster == null ) {
			roster = new HashMap<Staff, Timeslot>();
		}
		roster.put(staff, availability);
	}

	public List<String> getRequiredQualifications() {
		return requiredQualifications;
	}

	public void setRequiredQualifications(List<String> requiredQualifications) {
		this.requiredQualifications = requiredQualifications;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Timeslot getSchedule() {
		return schedule;
	}

	public void setSchedule(Timeslot schedule) {
		this.schedule = schedule;
	}

	@Override
	public String toString() {
		return "Task [name=" + name + ", workload=" + workload + ", roster=" + roster + ", requiredQualifications="
				+ requiredQualifications + ", location=" + location + ", schedule=" + schedule + "]";
	}

	public static void main(String[] args) {
		Task hrtC = new Task("High Risk Team C");
		
		Workload workload = new Workload();
		workload.getRequiredStaff().put("Provider", 3);
		workload.getRequiredStaff().put("Clinical", 2);
		workload.getRecommendedStaff().put("Clinical", 3);
		hrtC.workload = workload;
		
		Location location = new Location();
		location.setName("Purple Orchid");
		hrtC.location = location;
		
		hrtC.schedule = new Timeslot(LocalDateTime.of(2018, Month.SEPTEMBER, 4, 13, 0), LocalDateTime.of(2018, Month.SEPTEMBER, 4, 17, 0));;
		
		System.out.println(hrtC);
	}


}
