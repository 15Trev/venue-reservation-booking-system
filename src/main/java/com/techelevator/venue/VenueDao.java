package com.techelevator.venue;

import java.util.List;
import java.util.Map;

public interface VenueDao {

	public Venue save(Venue venue);
	public void update(Venue venue);
	public void delete(Long venueId);
	
	public Map<String, Venue> findAllVenues();
	public Venue findVenueById(Long venueId);
	List<String> getListOfAllCategories();
	List<String> getListOfCategoryNamesByVenue(Long venueId);
	String findVenueNameBySpaceId(Long spaceId);
	String getVenueLocation(Long cityId);
	boolean inCategory(List<String> categories, Long spaceId);
	
	
}
