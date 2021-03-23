package com.techelevator;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.venue.JdbcVenueDao;
import com.techelevator.venue.Venue;


public class JdbcVenueDaoIntegrationTest extends DAOIntegrationTest{

	private static DataSource dataSource = DAOIntegrationTest.getDataSource();
	private JdbcVenueDao venueDao;
	private JdbcTemplate jdbc = new JdbcTemplate(dataSource);
	private Long spaceId;
	
	@Before
	public void setup() {
		venueDao = new JdbcVenueDao(dataSource);
	}
	
	@Test
	public void save() {
		int beforeSave = venueDao.findAllVenues().size();
		venueDao.save(makeVenue());
		int afterSave = venueDao.findAllVenues().size();
	
		Assert.assertEquals(beforeSave +1, afterSave);
	}
	
	@Test
	public void update() {
		Venue venue = makeVenue();
		venueDao.save(venue);
		venue.setVenueName("Ben's House");
		
		venueDao.update(venue);
		Long venueId = venue.getVenueId();
		
		Assert.assertEquals(venue.getVenueName(), venueDao.findVenueById(venueId).getVenueName());
	}
	
	@Test
	public void delete() {
		Venue venue = venueDao.save(makeVenue());
		
		int test = venueDao.findAllVenues().size();
		venueDao.delete(venue.getVenueId());
		int result = venueDao.findAllVenues().size();
		
		Assert.assertEquals(test-1, result);
	}
	
	@Test
	public void find_All_Venues() {
		int setup = venueDao.findAllVenues().size();
		venueDao.save(makeVenue());
		int results = venueDao.findAllVenues().size();
		
		Assert.assertEquals(setup+1, results);
	}
	
	@Test
	public void find_Venue_By_Id() {
		Venue venue = venueDao.save(makeVenue());
		Venue resultVenue = venueDao.findVenueById(venue.getVenueId());
		Long testId = venue.getVenueId();
		Long resultId = resultVenue.getVenueId();
		Assert.assertEquals(testId, resultId);
	}
	@Test
	public void get_List_Of_All_Categories() {
		int test = venueDao.getListOfAllCategories().size();
		makeAll();
		int result = venueDao.getListOfAllCategories().size();
		
		Assert.assertEquals(test+1,result);
	}
	
	@Test
	public void find_Venue_Name_By_Space_Id() {
		String venueName = makeAll().getVenueName();
		String result = venueDao.findVenueNameBySpaceId(spaceId);
		
		Assert.assertEquals(venueName, result);
	}

	@Test
	public void get_Venue_Location() {
		Venue venue = makeVenue();
		venueDao.save(venue);
		String state = venue.getStateAbbrev();
		String city = venue.getCityName();
		
		String result = venueDao.getVenueLocation(venue.getCityId());
		Assert.assertEquals(city + ", " + state, result);
	}
	@Test
	public void venue_in_category() {
		makeAll();
		List<String> categories = venueDao.getListOfAllCategories();
		Assert.assertTrue(venueDao.inCategory(categories, spaceId));
	}
	
	@Test
	public void get_List_Of_Category_Names_By_Venue() {
		Venue venue = venueDao.save(makeVenue());
		
		int test = venueDao.getListOfCategoryNamesByVenue(venue.getVenueId()).size();
		makeCategory(venue);
		int result = venueDao.getListOfCategoryNamesByVenue(venue.getVenueId()).size();
		
		Assert.assertEquals(test+1, result);
	}
	
	private Venue makeVenue() {
		Venue venue = new Venue();
		venue.setCityName("Test City");
		venue.setDescription("Test City Description");
		venue.setStateAbbrev("TV");
		venue.setStateName("Trevor");
		venue.setVenueName("Test Venue");
		venue.setCategoryName("Test Category");
		return venue;
	}
	
	private Venue makeAll() {
		Venue venue = makeVenue();
		
		String sql = "INSERT INTO state VALUES (?, ?)";
		jdbc.update(sql, venue.getStateAbbrev(), venue.getStateName());
		
		sql = "INSERT INTO city VALUES (DEFAULT, ?, ?) RETURNING id";
		SqlRowSet row = jdbc.queryForRowSet(sql, venue.getCityName(), venue.getStateAbbrev());
		row.next();
		venue.setCityId(row.getLong("id"));
		
		sql = "INSERT INTO venue VALUES (DEFAULT, ?, ?, ?) RETURNING id";
		row = jdbc.queryForRowSet(sql, venue.getVenueName(), venue.getCityId(), venue.getDescription());
		row.next();
		venue.setVenueId(row.getLong("id"));
		
		sql = "INSERT INTO space VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
		row = jdbc.queryForRowSet(sql, venue.getVenueId(), "Test Space", false, null, null, 3000, 300);
		row.next();
		this.spaceId = row.getLong("id");
		
		sql = "INSERT INTO category VALUES (DEFAULT, ?) RETURNING id";
		row = jdbc.queryForRowSet(sql, venue.getCategoryName());
		row.next();
		venue.setCategoryId(row.getLong("id"));
		
		sql = "INSERT INTO category_venue VALUES (?, ?)";
		jdbc.update(sql, venue.getVenueId(), venue.getCategoryId());
		
		return venue;
	}
	
	private void makeCategory(Venue venue) {
		String sql = "INSERT INTO category VALUES (DEFAULT, ?) RETURNING id";
		SqlRowSet row = jdbc.queryForRowSet(sql, venue.getCategoryName());
		row.next();
		venue.setCategoryId(row.getLong("id"));
		
		sql = "INSERT INTO category_venue VALUES (?, ?)";
		jdbc.update(sql, venue.getVenueId(), venue.getCategoryId());
	}
}
