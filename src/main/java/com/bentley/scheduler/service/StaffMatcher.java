package com.bentley.scheduler.service;

import java.util.List;

import com.bentley.scheduler.model.Staff;
import com.bentley.scheduler.model.Task;

public class StaffMatcher {
	
	// TODO: findAlternateMatch. Use current schedule roster and remaining staffList to move people around as needed
	
	// TODO: Need to consider times (how to handle half shifts?)
	public void assignBestMatch( Task task, List<Staff> staffList ) {
		Staff match = null;
		if( task == null || staffList == null ) {
			return;
		}
		
		if( task.getRequiredQualifications() != null ) {
			for( String qualification : task.getRequiredQualifications() ) {
				qualification = qualification == null || qualification.trim().length() == 0 ? null : qualification.trim();
				match = findMinimumQualified( qualification, staffList);
				if( match != null ) {
					break;
				}
			}
		}
		// If nobody matches the qualification return the person with the least qualifications
		if( match == null ) {
			match = findMinimumQualified( null, staffList);
		}
		if( match != null ) {
			task.addStaff(match, task.getSchedule());//TODO: Need to use staff availability
			staffList.remove(match);
		}
	}
	
	private Staff findMinimumQualified( String qualification, List<Staff> staffList ) {
		Staff match = null;
		int matchQualifications = -1;
		for( Staff staff : staffList ) {
			if( qualification == null || staff.getQualifications().contains(qualification) ) {
				if( staff.getQualifications().size() > matchQualifications ) {
					match = staff;
					matchQualifications = staff.getQualifications().size();
				}
			}
		}
		
		return match;
	}

}
