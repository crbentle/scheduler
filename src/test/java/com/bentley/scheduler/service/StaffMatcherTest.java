package com.bentley.scheduler.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.bentley.scheduler.model.Staff;
import com.bentley.scheduler.model.Timeslot;

public class StaffMatcherTest {
	static StaffMatcher matcher;

	@BeforeClass
	public static void initClass() {
		matcher = new StaffMatcher();
	}
	
	@Test
	public void testIsAvailable_null() {
		Staff staff = new Staff("Test", "test");
		Timeslot timeframe = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 13, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0));

		assertFalse(matcher.isAvailable(null, null));
		assertFalse(matcher.isAvailable(timeframe, null));
		assertFalse(matcher.isAvailable(null, staff));
		
		staff.addAvailability( timeframe );
		assertFalse(matcher.isAvailable(null, staff));
	}
	
	@Test
	public void testIsAvailable_emptyTimeframe() {
		Staff staff = new Staff("Test", "test");
		Timeslot timeframe = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 13, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 13, 0));
		

		Timeslot availability = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 18, 0));
		staff.addAvailability( availability );
		
		assertFalse( matcher.isAvailable(timeframe, null));
		assertTrue( matcher.isAvailable(timeframe, staff));
		
		availability = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 9, 0));
		staff.getAvailability().clear();
		staff.addAvailability( availability );
		assertFalse( matcher.isAvailable(timeframe, staff));
	}

	@Test
	public void testIsAvailable_full() {
		Staff staff = new Staff("Test", "test");
		Timeslot timeframe = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 13, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0) );

		Timeslot availability = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 18, 0));
		staff.addAvailability( availability );

		assertTrue(matcher.isAvailable(timeframe, staff) );
	}
	
	@Test
	public void testIsAvailable_partial_before() {
		Staff staff = new Staff("Test", "test");
		Timeslot timeframe = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 13, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0) );

		Timeslot availability = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 13, 1));
		staff.addAvailability( availability );

		assertTrue(matcher.isAvailable(timeframe, staff) );
	}
	
	@Test
	public void testIsAvailable_partial_after() {
		Staff staff = new Staff("Test", "test");
		Timeslot timeframe = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 13, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0) );

		Timeslot availability = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 14, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 18, 0));
		staff.addAvailability( availability );

		assertTrue(matcher.isAvailable(timeframe, staff) );
	}
	
	@Test
	public void testIsAvailable_partial_inside() {
		Staff staff = new Staff("Test", "test");
		Timeslot timeframe = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 13, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0) );

		Timeslot availability = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 14, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 16, 0));
		staff.addAvailability( availability );

		assertTrue(matcher.isAvailable(timeframe, staff) );
	}
	
	@Test
	public void testIsAvailable_notAvaialble() {
		Staff staff = new Staff("Test", "test");
		Timeslot timeframe = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 13, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0) );

		Timeslot availability = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 13, 0));
		staff.addAvailability( availability );

		assertFalse(matcher.isAvailable(timeframe, staff) );
		
		availability = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 18, 0));
		staff.getAvailability().clear();
		staff.addAvailability( availability );

		assertFalse(matcher.isAvailable(timeframe, staff) );
	}
	
	@Test
	public void testIsQualified() {
		assertTrue( matcher.isFullyQualified( null, null ) );

		List<String> qualifications = new ArrayList<String>();
		assertTrue( matcher.isFullyQualified( qualifications, null ) );

		qualifications.add( "Q1" );
		assertFalse( matcher.isFullyQualified( qualifications, null ) );

		Staff staff = new Staff( "Test", "test" );
		assertFalse( matcher.isFullyQualified( qualifications, staff ) );

		staff.getQualifications().add( "Q2" );
		assertFalse( matcher.isFullyQualified( qualifications, staff ) );

		staff.getQualifications().add( "Q1" );
		assertTrue( matcher.isFullyQualified( qualifications, staff ) );

		qualifications.add( "Q2" );
		assertTrue( matcher.isFullyQualified( qualifications, staff ) );
	}

	@Test
	public void testFindBestMatch_singleStaff() {
		// Not qualified
		Staff person1 = createStaff(
				"Person1", "test", 
				new ArrayList<String>(), 
				new Timeslot(
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0))
				);
		
		List<Staff> staffList = new ArrayList<Staff>(Arrays.asList( person1 ));
		
		Timeslot timeframe = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 12, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0));
		
		List<String> qualifications = new ArrayList<String>(Arrays.asList( "Q1", "Q3" ));
		
		Staff match = matcher.findBestMatch( qualifications, timeframe, staffList );
		assertNotNull(match);
		assertEquals("Person1", match.getName());

		// Qualified
		person1 = createStaff(
				"Person1", "test", 
				Arrays.asList( "Q1", "Q2", "Q3" ), 
				new Timeslot(
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0))
				);
		
		staffList = new ArrayList<Staff>(Arrays.asList( person1 ));
		qualifications = new ArrayList<String>(Arrays.asList( "Q1", "Q3" ));
		
		match = matcher.findBestMatch( qualifications, timeframe, staffList );
		assertNotNull(match);
		assertEquals("Person1", match.getName());

		// No required qualifications
		person1 = createStaff(
				"Person1", "test", 
				Arrays.asList( "Q1", "Q2", "Q3" ), 
				new Timeslot(
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0))
				);
		
		staffList = new ArrayList<Staff>(Arrays.asList( person1 ));
		qualifications = new ArrayList<String>();
		
		match = matcher.findBestMatch( qualifications, timeframe, staffList );
		assertNotNull(match);
		assertEquals("Person1", match.getName());

		// Not available
		person1 = createStaff(
				"Person1", "test", 
				Arrays.asList( "Q1", "Q2", "Q3" ), 
				new Timeslot(
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 11, 30))
				);
		
		staffList = new ArrayList<Staff>(Arrays.asList( person1 ));
		qualifications = new ArrayList<String>();
		
		match = matcher.findBestMatch( qualifications, timeframe, staffList );
		assertNull(match);
	}

	@Test
	public void testFindBestMatch_ByAvailability() {
		Staff person1 = createStaff(
				"Person1", "test", 
				null, 
				new Timeslot(
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 11, 0))
				);

		Staff person2 = createStaff(
				"Person2", "test", 
				null, 
				new Timeslot(
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0))
				);

		Staff person3 = createStaff(
				"Person3", "test", 
				null, 
				new Timeslot(
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 13, 0),
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0))
				);
		
		List<Staff> staffList = new ArrayList<Staff>(Arrays.asList( person1, person2, person3 ));
		
		Timeslot timeframe = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 11, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 13, 0));
		
		Staff match = matcher.findBestMatch( null, timeframe, staffList );
		assertNotNull(match);
		assertEquals("Person2", match.getName());
	}

	@Test
	public void testFindBestMatch_Qualifications() {
		Staff person1 = createStaff(
				"Person1", "test", 
				new ArrayList<String>(Arrays.asList( "Q1")), 
				new Timeslot(
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0))
				);

		Staff person2 = createStaff(
				"Person2", "test", 
				new ArrayList<String>(Arrays.asList( "Q2" )), 
				new Timeslot(
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0))
				);

		Staff person3 = createStaff(
				"Person3", "test", 
				new ArrayList<String>(Arrays.asList( "Q3" )), 
				new Timeslot(
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0))
				);
		
		List<Staff> staffList = new ArrayList<Staff>(Arrays.asList( person1, person2, person3 ));
		
		Timeslot timeframe = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 12, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0));
		
		List<String> qualifications = new ArrayList<String>(Arrays.asList( "Q2" ));
		
		Staff match = matcher.findBestMatch( qualifications, timeframe, staffList );
		assertNotNull(match);
		assertEquals("Person2", match.getName());
		
		person1.addQualification( "Q2" );
		qualifications = new ArrayList<String>(Arrays.asList( "Q1", "Q2" ));
		
		match = matcher.findBestMatch( qualifications, timeframe, staffList );
		assertNotNull(match);
		assertEquals("Person1", match.getName());
		
		// TODO: If no one is fully qualified get next best qualified
		person1.setQualifications( new ArrayList<String>( Arrays.asList( "Q1" ) ) );
		person2.setQualifications( new ArrayList<String>( Arrays.asList( "Q1", "Q2" ) ) );
		person3.setQualifications( new ArrayList<String>( Arrays.asList( "Q3" ) ) );
		qualifications = new ArrayList<String>(Arrays.asList( "Q1", "Q2", "Q3" ));
		
		match = matcher.findBestMatch( qualifications, timeframe, staffList );
		assertNotNull(match);
		assertEquals("Person2", match.getName());
	}

	@Test
	public void testFindBestMatch_QualificationCount() {
		Staff person1 = createStaff(
				"Person1", "test", 
				Arrays.asList( "Q1", "Q2", "Q3" ), 
				new Timeslot(
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0))
				);

		Staff person2 = createStaff(
				"Person2", "test", 
				Arrays.asList( "Q1", "Q3" ), 
				new Timeslot(
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0))
				);
		Staff person3 = createStaff(
				"Person3", "test", 
				Arrays.asList( "Q1", "Q2", "Q3" ), 
				new Timeslot(
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 8, 0),
						LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0))
				);
		
		List<Staff> staffList = new ArrayList<Staff>(Arrays.asList( person1, person2, person3 ));
		
		Timeslot timeframe = new Timeslot(
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 12, 0),
				LocalDateTime.of(2018, Month.SEPTEMBER, 9, 17, 0));
		
		List<String> qualifications = new ArrayList<String>(Arrays.asList( "Q1" ));
		
		Staff match = matcher.findBestMatch( qualifications, timeframe, staffList );
		assertNotNull(match);
		assertEquals("Person2", match.getName());
	}
	
	private Staff createStaff( String name, String type, List<String> qualifications, Timeslot...availability ) {
		Staff staff = new Staff(name, type);
		staff.setQualifications( qualifications );
		if( availability != null ) {
			for( Timeslot timeframe : availability ) {
				staff.addAvailability( timeframe );
			}
		}
		return staff;
	}
}
