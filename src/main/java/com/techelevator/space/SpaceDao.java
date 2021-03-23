package com.techelevator.space;

import java.time.LocalDate;
import java.util.List;

public interface SpaceDao {

	public void save(Space space);
	
	public void update(Space space);
	
	public void delete(Long spaceId);
	
	public List<Space> findAllSpaces();
	
	public Space findSpaceById(Long spaceId);
	
	public List<Space> findSpaceByVenueId(Long venueId);
	
	public List<Space> findSpaceByOpenDates(LocalDate openFrom, LocalDate openTo, Long venueId);
	
	public List<Space> findSpaceByAccessibility(boolean isAccessible);

	public List<Space> findSpaceByMaxOccupancy(int maxOccupancy, Long venueId);

	boolean isAccessibile(boolean isAccessible, Long spaceId);

	boolean canOccupy(int maxOccupancy, Long spaceId);

	boolean isSpaceOpen(LocalDate bookDate, int duration, Long spaceId);

	boolean isAffordable(int dailyBudget, Long spaceId);
	
}
