package com.techelevator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.techelevator.reservation.Reservation;
import com.techelevator.space.Space;
import com.techelevator.venue.Venue;
import com.techelevator.view.DatabaseReader;

public class ExcelsiorController {

	private static DatabaseReader reader = new DatabaseReader();
	private List<String> venueList;
	private Map<String, Venue> venueMap = reader.venue.findAllVenues();
	public String venueNameForSpaceDetails;
	public Long venueIdForSpaceDetails;
	private int confirmationNumber = 37463929;
	
	public List<String> getVenues(){
		venueList = new ArrayList<String>();
		
		for(Entry<String, Venue> e: venueMap.entrySet()) {
			venueList.add(e.getKey());
		}
		return venueList;
	}
	
	public String getVenueDetailsPage(int id) {
		String venueName = venueList.get(id);
		venueNameForSpaceDetails = venueName;
		Long cityId = venueMap.get(venueName).getCityId();
		Long venueId = venueMap.get(venueName).getVenueId();
		venueIdForSpaceDetails = venueId;
		Venue venue = reader.venue.findVenueById(venueId);
		return venue.getVenueName() + "\n"
				+ "Location: " + reader.venue.getVenueLocation(cityId) + "\n"
				+ "Categories: " + getVenueCategoryList(venueId) + "\n\n"
				+ venue.getDescription() + "\n";
	}
	
	private String getVenueCategoryList(Long venueId) {
		List<String> list = reader.venue.getListOfCategoryNamesByVenue(venueId);
		String categories = "";
		for(String category: list) {
			categories += (category + ", ");
		}
		return categories;
	}
	
	public List<Space> getSpaces(Long venueId) {
		List<Space> spaceList = reader.space.findSpaceByVenueId(venueId);
		return spaceList;
	}

	
	public List<Space> findUsableSpaces(LocalDate date, int duration, int attendance) {
		List<Space> venueSpaces = getSpaces(venueIdForSpaceDetails);
		List<Space> availableSpaces = new ArrayList<Space>();
		
		for(Space s: venueSpaces) {
			if(!reader.space.canOccupy(attendance, s.getSpaceId())) { continue;	} 
			else if(!reader.space.isSpaceOpen(date, duration, s.getSpaceId())) { continue; }
			else if(reader.reserve.isReserved(date, duration, s.getSpaceId())) { continue; }
			else availableSpaces.add(s); 
		}
		
		return availableSpaces;
	}
	
	public Map<String, List<Space>> findUsableSpaces(LocalDate date, int duration, int attendance, boolean accessible, int budget, List<String> categories) {
		List<Space> allSpaces = reader.space.findAllSpaces();
		Map<String, List<Space>> finalMap = new HashMap<String, List<Space>>();
		
		for(Space s: allSpaces) {
			String venueName = reader.venue.findVenueById(s.getVenueId()).getVenueName();
			List<Space> venueSpaces;
			if(finalMap.containsKey(venueName)) {
				venueSpaces = finalMap.get(venueName);
			} else {
				venueSpaces = new ArrayList<Space>();
			}
			
			if(!reader.space.canOccupy(attendance, s.getSpaceId())) { continue;	} 
			else if(!reader.space.isSpaceOpen(date, duration, s.getSpaceId())) { continue; }
			else if(reader.reserve.isReserved(date, duration, s.getSpaceId())) { continue; }
			else if(accessible && !reader.space.isAccessibile(accessible, s.getSpaceId())) { continue; }
			else if (reader.space.isAffordable(budget, s.getSpaceId())) { continue; }
			else if (!categories.isEmpty() && !reader.venue.inCategory(categories, s.getSpaceId())) { continue; }
			else venueSpaces.add(s);
			
			finalMap.put(venueName, venueSpaces);

		}
		return finalMap;
		
	}
	
	public String makeReservation(Long spaceId, String reservationName, LocalDate date, int duration, int attendance) {
		Reservation reservation = new Reservation();
		reservation.setSpaceId(spaceId);
		reservation.setNumberOfAttendees(attendance);
		reservation.setStartDate(date);
		reservation.setEndDate(date.plusDays(duration));
		reservation.setReservedFor(reservationName);
		
		String venueName = reader.venue.findVenueNameBySpaceId(spaceId);
		
		Long reservationId = reader.reserve.save(reservation);  // use with confirmation number to store
		this.confirmationNumber++;

		String confirmation = "Confirmation #: " + confirmationNumber + "\n"
							+ "Venue: " + venueName + "\n"
							+ "Space: " + reader.space.findSpaceById(spaceId).getName() + "\n"
							+ "Reserved for: " + reservationName + "\n"
							+ "Attendees: " + attendance + "\n"
							+ "Arrival Date: " + date + "\n"
							+ "Depart Date: " + date.plusDays(duration) + "\n"
							+ "Total Cost: $" + reader.space.findSpaceById(spaceId).getDailyRate() * duration;
		
		
		return confirmation;

	}
	
	public List<Reservation> getReservations(String reservationName) {
		List<Reservation> reservations = new ArrayList<Reservation>();
		reservations = reader.reserve.findReservationByName(reservationName, venueIdForSpaceDetails);
		return reservations;
	}
	
	public List<String> getCategoryList() {
		List<String> categoryList = reader.venue.getListOfAllCategories();
		return categoryList;
	}

	public List<Reservation> findUpcomingReservations() {
		List<Reservation> r = new ArrayList<Reservation>();
		LocalDate date = LocalDate.now();
		for(int i = 0; i < 30; i ++) {
			
			r.addAll(reader.reserve.findReservationByStartDate(date));
			date = date.plusDays(1);
		}
		return r;
	}
	
}
