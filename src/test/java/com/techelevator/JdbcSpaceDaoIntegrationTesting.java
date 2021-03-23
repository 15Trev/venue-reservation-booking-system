package com.techelevator;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.reservation.JdbcReservationDao;
import com.techelevator.space.JdbcSpaceDao;
import com.techelevator.space.Space;
import com.techelevator.venue.Venue;

public class JdbcSpaceDaoIntegrationTesting extends DAOIntegrationTest {

	private static DataSource dataSource = DAOIntegrationTest.getDataSource();
	private JdbcSpaceDao spaceDao;
	private JdbcTemplate jdbc = new JdbcTemplate(dataSource);
	private Long venueId;
	private Space space;
	
	@Before
	public void setup() {
		spaceDao = new JdbcSpaceDao(dataSource);

	}
	
	@Test
	public void save_space() {
		int oldListAmount = spaceDao.findAllSpaces().size();
		
		makeAll();
		spaceDao.save( makeSpace() );
		
		int newListAmount = spaceDao.findAllSpaces().size();
		Assert.assertEquals(oldListAmount + 1, newListAmount);
	}
	
	@Test
	public void update_space() {
		makeAll();
		spaceDao.save( makeSpace() );
		space.setAccessable(true);
		spaceDao.update(space);
		
		List<Space> list = spaceDao.findSpaceByAccessibility(true);	
		boolean secondAccessible = list.get(list.size()-1).isAccessible();
		
		Assert.assertTrue(secondAccessible);
	}
	
	@Test
	public void delete_space() {
		makeAll();
		makeSpace();
		spaceDao.save( space );
		int firstCount = spaceDao.findAllSpaces().size();
		spaceDao.delete(space.getSpaceId());
		int secondCount = spaceDao.findAllSpaces().size();
		
		Assert.assertEquals(firstCount-1, secondCount);
		
	}
	@Test
	public void findSpaceById() {
		makeAll();
		spaceDao.save(makeSpace());
		Long testId = space.getSpaceId();
		
		Assert.assertEquals(space, spaceDao.findSpaceById(testId));
	}
	
	@Test
	public void findSpaceByVenueId(){
		makeAll();
		spaceDao.save(makeSpace());
		Long testId = space.getVenueId();
		
		Assert.assertEquals(space, spaceDao.findSpaceByVenueId(testId).get(0));
	}
	
//	@Test
//	public void findSpaceByOpenDates(){
//		makeAll();
//		spaceDao.save(makeSpace());
//		int testOpenFromDate = space.getOpenFrom();
//		int testOpenToDate = space.getOpenTo();
//		List<Space> testList = spaceDao.findSpaceByOpenDates(testOpenFromDate, testOpenToDate);
//		
//		Assert.assertEquals(space, spaceDao.findSpaceByOpenDates(testOpenFromDate, testOpenToDate).get(testList.size()-1));
//	}
	
	@Test
	public void findSpaceByAccessability(){
		makeAll();
		spaceDao.save(makeSpace());
		boolean accessible = space.isAccessible();
		List<Space> testList = spaceDao.findSpaceByAccessibility(accessible);
		
		Assert.assertEquals(space, spaceDao.findSpaceByAccessibility(accessible).get(testList.size()-1));
	}
	
//	@Test
//	public void findSpaceByMaxOccupancy() {
//		makeAll();
//		spaceDao.save(makeSpace());
//		int maxOccupancy = space.getMaxOccupancy();
//		List<Space> testList = spaceDao.findSpaceByMaxOccupancy(maxOccupancy);
//		
//		Assert.assertEquals(space, spaceDao.findSpaceByMaxOccupancy(maxOccupancy).get(testList.size()-1));
//	}
	
	
	
	
	private Space makeSpace() {
		Space newSpace = new Space();
		newSpace.setVenueId(venueId);
		newSpace.setName("Test Space");
		newSpace.setAccessable(false);
		newSpace.setOpenFrom(1);
		newSpace.setOpenTo(3);
		newSpace.setDailyRate(4000);
		newSpace.setMaxOccupancy(201);
		this.space = newSpace;
		return newSpace;
	}
	
	private Venue makeVenue() {
		Venue venue = new Venue();
		venue.setCityName("Test City");
		venue.setDescription("Test City Description");
		venue.setStateAbbrev("TV");
		venue.setStateName("Trevor");
		venue.setVenueName("Test Venue");
		return venue;
	}
	
	private void makeAll() {
		Venue venue = makeVenue();
		String sql = "INSERT INTO state VALUES (?,?)";
		jdbc.update(sql, venue.getStateAbbrev(), venue.getStateName());
		
		sql = "INSERT INTO city VALUES (DEFAULT, ? , ?) RETURNING id";
		SqlRowSet row = jdbc.queryForRowSet(sql, venue.getCityName(), venue.getStateAbbrev());
		row.next();
		Long cityId = row.getLong("id");
		
		sql = "INSERT INTO venue VALUES (DEFAULT, ?, ?, ?) RETURNING id";
		row = jdbc.queryForRowSet(sql, venue.getVenueName(), cityId, venue.getDescription());
		row.next();
		this.venueId = row.getLong("id");
	}
}
