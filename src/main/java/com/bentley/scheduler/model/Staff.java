package com.bentley.scheduler.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Staff {

	String name;
	String type;
	List<String> qualifications = new ArrayList<String>();
	/* Ordered list of location priorities*/
	List<String> locationPriority;
	//TODO: assignments (assigned tasks/hours)

	/*
	 * Map of Date/Availability //Ordered list of availability from earliest to
	 * latest
	 */
	Map<LocalDate, Timeslot> availability = new TreeMap<LocalDate, Timeslot>();

	int hoursPerPeriod;
	int hoursWorked;

	// goals
	
	public Staff( String name, String type ) {
		this.name = name;
		this.type = type;
	}
	
	public static void main(String[] args) {
		Staff debbie = new Staff( "Debbie", "Clinical");

		debbie.setQualifications(new ArrayList<String>(Arrays.asList("RN", "NST")));
		debbie.setLocationPriority(new ArrayList<String>(Arrays.asList("Other", "NOB")));

		debbie.availability.put(LocalDate.of(2018, Month.SEPTEMBER, 4), new Timeslot(LocalDateTime.of(2018, Month.SEPTEMBER, 4, 7, 30), LocalDateTime.of(2018, Month.SEPTEMBER, 4, 17, 0)));
		debbie.availability.put(LocalDate.of(2018, Month.SEPTEMBER, 5), new Timeslot(LocalDateTime.of(2018, Month.SEPTEMBER, 5, 9, 00), LocalDateTime.of(2018, Month.SEPTEMBER, 5, 15, 0)));
		
		debbie.setHoursPerPeriod(40);
		debbie.setHoursWorked(24);
		System.out.println(debbie);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getQualifications() {
		return qualifications;
	}

	public void setQualifications(List<String> qualifications) {
		this.qualifications = qualifications;
	}
	
	public void addQualification(String qualification) {
		if( qualification != null && qualification.trim().length() > 0 ) {
			qualifications.add( qualification.trim() );
		}
	}

	public List<String> getLocationPriority() {
		return locationPriority;
	}

	public void setLocationPriority(List<String> locationPriority) {
		this.locationPriority = locationPriority;
	}

	public Map<LocalDate, Timeslot> getAvailability() {
		return availability;
	}

	public void setAvailability(Map<LocalDate, Timeslot> availability) {
		this.availability = availability;
	}
	
	public void addAvailability( Timeslot timeframe ) {
		if( timeframe != null && timeframe.getStartTime() != null ) {
			LocalDate date = timeframe.getStartTime().toLocalDate();
			availability.put( date, timeframe );
		}
	}

	public int getHoursPerPeriod() {
		return hoursPerPeriod;
	}

	public void setHoursPerPeriod(int hoursPerPeriod) {
		this.hoursPerPeriod = hoursPerPeriod;
	}

	public int getHoursWorked() {
		return hoursWorked;
	}

	public void setHoursWorked(int hoursWorked) {
		this.hoursWorked = hoursWorked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((availability == null) ? 0 : availability.hashCode());
		result = prime * result + hoursPerPeriod;
		result = prime * result + hoursWorked;
		result = prime * result + ((locationPriority == null) ? 0 : locationPriority.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((qualifications == null) ? 0 : qualifications.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Staff other = (Staff) obj;
		if (availability == null) {
			if (other.availability != null)
				return false;
		} else if (!availability.equals(other.availability))
			return false;
		if (hoursPerPeriod != other.hoursPerPeriod)
			return false;
		if (hoursWorked != other.hoursWorked)
			return false;
		if (locationPriority == null) {
			if (other.locationPriority != null)
				return false;
		} else if (!locationPriority.equals(other.locationPriority))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (qualifications == null) {
			if (other.qualifications != null)
				return false;
		} else if (!qualifications.equals(other.qualifications))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Staff [name=" + name + ", type=" + type + ", qualifications=" + qualifications + ", locationPriority="
				+ locationPriority + ", availability=" + availability + ", hoursPerPeriod=" + hoursPerPeriod
				+ ", hoursWorked=" + hoursWorked + "]";
	}

}
