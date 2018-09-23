package com.bentley.scheduler.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.bentley.scheduler.model.Staff;
import com.bentley.scheduler.model.Timeslot;

public class StaffMatcher {

	// TODO: findAlternateMatch. Use current schedule roster and remaining staffList
	// to move people around as needed

	// TODO: Need to consider times (how to handle half shifts?)
	public Staff findBestMatch( List<String> qualifications, Timeslot timeframe, List<Staff> staffList ) {
		Staff match = null;
		if (timeframe == null || staffList == null) {
			return match;
		}

		match = findMinimumQualified( qualifications, timeframe, staffList );

		return match;
	}

	/**
	 * Loop through the list of Staff and find the best match for the given qualifications and time slot.
	 * If one or more staff members are found that match all required qualifications the staff with the smallest
	 * number of qualifications will be returned.
	 * 
	 * If no staff matches all qualifications the staff member with the most required qualifications (weighted by
	 * qualification priority) will be returned.
	 * 
	 * @param qualifications The list of required qualifications
	 * @param timeslot The time slot of the task that needs a Staff member
	 * @param staffList The Staff pool to choose from
	 * @return The best matched Staff member
	 */
	private Staff findMinimumQualified( List<String> qualifications, Timeslot timeslot, List<Staff> staffList ) {
		if (staffList == null) {
			return null;
		}

		Staff match = null;
		boolean foundFullyQualified = false;
		int partiallyQualifiedScore = -1;
		int matchQualificationCount = Integer.MAX_VALUE;
		for (Staff staff : staffList) {
			if (isAvailable( timeslot, staff )) {
				if (isFullyQualified( qualifications, staff )) {

					foundFullyQualified = true;
					// Find the fully qualified person with the smallest number of overall qualifications
					int staffQualificationCount = staff.getQualifications() == null ? 0 : staff.getQualifications().size();
					if (staffQualificationCount < matchQualificationCount) {
						match = staff;
						matchQualificationCount = staffQualificationCount;
					}
				} else if (!foundFullyQualified) {
					// Find the most qualified staff that is not fully qualified
					// Needs to consider qualification priority
					int qualificationScore = getQualificationScore( qualifications, staff );
					if (qualificationScore > partiallyQualifiedScore) {
						match = staff;
						partiallyQualifiedScore = qualificationScore;
					}
				}
			}
		}

		return match;
	}

	private List<Staff> findAvailableStaff( Timeslot timeframe, List<Staff> staffList ) {
		List<Staff> availableStaff = new ArrayList<Staff>();
		if( timeframe == null || staffList == null ) {
			return availableStaff;
		}
		
		for( Staff staff : staffList ) {
//			LocalDate date = timeframe.getStartTime().toLocalDate();
//			Timeframe availability = staff.getAvailability().get(date);
			if( isAvailable( timeframe, staff ) ) {
				availableStaff.add(staff);
			}
		}
		
		return availableStaff;
	}

	protected boolean isAvailable(Timeslot timeframe, Staff staff) {
		if (timeframe == null || staff == null || staff.getAvailability() == null ) {
			return false;
		}
		
		LocalDate date = timeframe.getStartTime().toLocalDate();
		Timeslot availability = staff.getAvailability().get(date);
		if( availability == null ) {
			return false;
		}

		if (!availability.getStartTime().isBefore(availability.getEndTime())) {
			return false;
		}

		if (timeframe.getStartTime().isAfter(availability.getEndTime())
			|| timeframe.getStartTime().isEqual(availability.getEndTime())
			|| timeframe.getEndTime().isBefore(availability.getStartTime())
			|| timeframe.getEndTime().isEqual(availability.getStartTime())) {
			return false;
		}

		return true;
	}

	private List<Staff> findQualifiedStaff( List<String> requiredQualifications, List<Staff> availableStaff ) {
		List<Staff> qualifiedStaff = new ArrayList<Staff>();
		if( requiredQualifications == null || requiredQualifications.isEmpty() ) {
			return availableStaff;
		}
		if( availableStaff == null ) {
			return qualifiedStaff;
		}
		
		for( Staff staff : availableStaff ) {
			if( isFullyQualified( requiredQualifications, staff ) ) {
				qualifiedStaff.add(staff);
			}
		}
		
		return availableStaff;
	}
	
	protected boolean isFullyQualified( List<String> requiredQualifications, Staff staff ) {
		boolean qualified = false;
		if( requiredQualifications == null || requiredQualifications.isEmpty() ) {
			qualified = true;
		} else if( staff == null ) {
			qualified = false;
		} else if( staff.getQualifications().containsAll(requiredQualifications) ){
			qualified = true;
		}
		
		return qualified;
	}
	
	protected int getQualificationScore( List<String> requiredQualifications, Staff staff ) {
		if( requiredQualifications == null
			|| requiredQualifications.isEmpty()
			|| staff == null 
			|| staff.getQualifications() == null
			|| staff.getQualifications().isEmpty() ) {
			return 0;
		}

		int score = 0;
		
		for( int i = 0; i < requiredQualifications.size(); i++ ) {
			if( staff.getQualifications().contains( requiredQualifications.get( i ) ) ) {
				score += ( requiredQualifications.size() - i ) * requiredQualifications.size();
			}
		}
		
		return score;
	}

}
