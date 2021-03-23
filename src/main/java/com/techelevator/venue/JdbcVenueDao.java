package com.techelevator.venue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcVenueDao implements VenueDao {

	private JdbcTemplate jdbc;
	
	public JdbcVenueDao(DataSource dataSource) {
		jdbc = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public Venue save(Venue venue) {
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
		return venue;
	}

	@Override
	public void update(Venue venue) {
		String sql = "UPDATE venue SET name = ?, city_id = ?, description = ?";
		jdbc.update(sql, venue.getVenueName(), venue.getCityId(), venue.getDescription());
	}

	@Override
	public void delete(Long venueId) {
		String sql = "DELETE FROM venue WHERE id = ?";
		jdbc.update(sql, venueId);
	}
	
	@Override
	public Map<String, Venue> findAllVenues() {
		String sql = "SELECT id AS venue_id, name AS venue_name, city_id AS city_id, description AS venue_description FROM venue";
		SqlRowSet row = jdbc.queryForRowSet(sql);
		
		Map<String, Venue> venues = new TreeMap<String, Venue>();
		while(row.next()) {
			venues.put(row.getString("venue_name"), mapRowToVenue(row) );
		}
		return venues;
	}
	
	@Override
	public List<String> getListOfAllCategories() {
		List<String> categoryNames = new ArrayList<String>();
		String sql = "SELECT name FROM category";
		SqlRowSet row = jdbc.queryForRowSet(sql);
		while(row.next()) {
			categoryNames.add(row.getString("name"));
		}
		return categoryNames;
	}
	
	@Override
	public List<String> getListOfCategoryNamesByVenue(Long venueId){
		String sql = "SELECT c.name FROM category c " +
				"JOIN category_venue cv ON c.id = cv.category_id " +
				"JOIN venue v ON cv.venue_id = v.id " +
				"WHERE v.id = ?";
		SqlRowSet row = jdbc.queryForRowSet(sql, venueId);
		List<String> list = new ArrayList<String>();
		while(row.next()) {
			list.add(row.getString("name"));
		}
		return list;
	}
	
	@Override
	public Venue findVenueById(Long venueId) {
		String sql = "SELECT id AS venue_id, name AS venue_name, city_id AS city_id, description AS venue_description FROM venue WHERE id = ?";
		SqlRowSet row = jdbc.queryForRowSet(sql, venueId);
		row.next();
			return mapRowToVenue(row);
	}

	@Override
	public String findVenueNameBySpaceId(Long spaceId) {
		String sql = "SELECT venue.name AS venue_name FROM venue "
					+ "JOIN space ON venue.id = space.venue_id "
					+ "WHERE space.id = ?";
		SqlRowSet row = jdbc.queryForRowSet(sql, spaceId);
		row.next();
		return row.getString("venue_name");
	}
	
	@Override
	public String getVenueLocation(Long cityId) {
		String sql = "SELECT state_abbreviation, city.name AS city_name FROM city "
				   + "WHERE id = ?";
		SqlRowSet row = jdbc.queryForRowSet(sql, cityId);
		row.next();
		String state = row.getString("state_abbreviation");		
		String city = row.getString("city_name");
		return city + ", " + state;
	}
	
	@Override
	public boolean inCategory(List<String> categories, Long spaceId) {
		if(categories.isEmpty()) { return false; }
		boolean inCategory = false;
		for(int i = 0 ; i < categories.size(); i++) {
			String sql = "SELECT c.name AS category_name FROM category c "
					+ "JOIN category_venue cv ON c.id = cv.category_id "
					+ "JOIN venue v ON cv.venue_id = v.id "
					+ "JOIN space ON v.id = space.venue_id "
					+ "WHERE space.id = ? AND c.name = ?";
			SqlRowSet row = jdbc.queryForRowSet(sql, spaceId, categories.get(i) );
			if(row.next()) {
				inCategory = true;
			}
		}
		return inCategory;
	}
	
	private Venue mapRowToVenue(SqlRowSet result) {
		Venue venue = new Venue();
		venue.setVenueId(result.getLong("venue_id"));
		venue.setVenueName(result.getString("venue_name"));
		venue.setCityId(result.getLong("city_id"));
		venue.setDescription(result.getString("venue_description"));
		return venue;
	}
}
