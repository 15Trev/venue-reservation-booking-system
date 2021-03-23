package com.techelevator.space;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcSpaceDao implements SpaceDao {

	private JdbcTemplate jdbc;
	
	public JdbcSpaceDao(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void save(Space space) {
		String sql = "INSERT INTO space VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
		SqlRowSet row = jdbc.queryForRowSet(sql,
				space.getVenueId(), 
				space.getName(), 
				space.isAccessible(),
				space.getOpenFrom(),
				space.getOpenTo(),
				space.getDailyRate(),
				space.getMaxOccupancy());
		if(row.next()){
			space.setSpaceId(row.getLong("id"));
		}
		
	}

	@Override
	public void update(Space space) {
		String sql = "UPDATE space SET venue_id = ?, name = ?, is_accessible = ?, open_from = ?, open_to = ?, daily_rate = ?, max_occupancy = ?";
		jdbc.update(sql,
				space.getVenueId(),
				space.getName(),
				space.isAccessible(),
				space.getOpenFrom(),
				space.getOpenTo(),
				space.getDailyRate(),
				space.getMaxOccupancy());
	}

	@Override
	public void delete(Long spaceId) {
		String sql = "DELETE FROM space WHERE id = ?";
		jdbc.update(sql, spaceId);
	}
	
	@Override
	public List<Space> findAllSpaces() {
		String sql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::numeric, max_occupancy FROM space";
		SqlRowSet result = jdbc.queryForRowSet(sql);
		List<Space> list = new ArrayList<Space>();
		while(result.next()) {
			list.add(mapResultToSpace(result));
		}
		return list;
	}
	
	@Override
	public Space findSpaceById(Long spaceId) {
		String sql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::numeric, max_occupancy FROM space WHERE id = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, spaceId);
		if(result.next()) {
			Space space = mapResultToSpace(result);
			return space;
		}
		return null;
	}

	@Override
	public List<Space> findSpaceByVenueId(Long venueId) {
		String sql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::numeric, max_occupancy FROM space WHERE venue_id = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, venueId);
		List<Space> spaceList = new ArrayList<Space>();
		while(result.next()) {
			spaceList.add( mapResultToSpace(result) );
		}
		return spaceList;
	}

	@Override
	public List<Space> findSpaceByOpenDates(LocalDate bookDate, LocalDate leaveDate, Long venueId) {
		String bookAsString = bookDate.toString();
		String[] bookAsStringSplit = bookAsString.split("-");
		int openFrom = Integer.parseInt(bookAsStringSplit[1]);
		String leaveAsString = leaveDate.toString();
		String[] leaveAsStringSplit = leaveAsString.split("-");
		int openTo = Integer.parseInt(leaveAsStringSplit[1]);
		
		String sql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::numeric, max_occupancy FROM space WHERE open_from IS NULL AND open_to IS NULL AND venue_id = ?";
		List<Space> spaceList = new ArrayList<Space>();
		SqlRowSet result = jdbc.queryForRowSet(sql, venueId);
		while(result.next()) {
			spaceList.add( mapResultToSpace(result) );
		}
		
		sql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::numeric, max_occupancy FROM space WHERE open_from <= ? AND open_to >= ? AND venue_id = ?";
		result = jdbc.queryForRowSet(sql, openFrom, openTo, venueId);
		while(result.next()) {
			spaceList.add( mapResultToSpace(result) );
		}
		return spaceList;
	}

	@Override
	public List<Space> findSpaceByAccessibility(boolean isAccessible) {
		String sql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::numeric, max_occupancy FROM space WHERE is_accessible = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, isAccessible);
		List<Space> spaceList = new ArrayList<Space>();
		while(result.next()) {
			spaceList.add( mapResultToSpace(result) );
		}
		return spaceList;
	}

	@Override
	public List<Space> findSpaceByMaxOccupancy(int maxOccupancy, Long venueId) {
		String sql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::numeric, max_occupancy FROM space WHERE max_occupancy >= ? AND venue_id = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, maxOccupancy, venueId);
		List<Space> spaceList = new ArrayList<Space>();
		while(result.next()) {
			spaceList.add( mapResultToSpace(result) );
		}
		return spaceList;
	}
	
	private Space mapResultToSpace(SqlRowSet result) {
		Space newSpace = new Space();
		newSpace.setSpaceId(result.getLong("id"));
		newSpace.setVenueId(result.getLong("venue_id"));
		newSpace.setName(result.getString("name"));
		newSpace.setAccessable(result.getBoolean("is_accessible"));
		newSpace.setOpenFrom(result.getInt("open_from"));
		newSpace.setOpenTo(result.getInt("open_to"));
		newSpace.setDailyRate(result.getDouble("daily_rate"));
		newSpace.setMaxOccupancy(result.getInt("max_occupancy"));
		return newSpace;
	}
	
	
	@Override
	public boolean isAccessibile(boolean isAccessible, Long spaceId) {
		String sql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::numeric, max_occupancy FROM space WHERE id = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, spaceId);
		result.next();
		if(isAccessible == result.getBoolean("is_accessible")) {
			return true;
		} else return false;
	}

	@Override
	public boolean canOccupy(int maxOccupancy, Long spaceId) {
		String sql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::numeric, max_occupancy FROM space WHERE id = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, spaceId);
		result.next();
		if (maxOccupancy <= result.getInt("max_occupancy")) {
			return true;
		} else return false;
	}
	
	@Override
	public boolean isSpaceOpen(LocalDate bookDate, int duration, Long spaceId) {
		String bookAsString = bookDate.toString();
		String[] bookAsStringSplit = bookAsString.split("-");
		int openFrom = Integer.parseInt(bookAsStringSplit[1]);
		
		String leaveAsString = bookDate.plusDays(duration).toString();
		String[] leaveAsStringSplit = leaveAsString.split("-");
		int openTo = Integer.parseInt(leaveAsStringSplit[1]);
		
		String sql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::numeric, max_occupancy FROM space WHERE id = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, spaceId);
		result.next();
		int openFromActual = result.getInt("open_from");
		int openToActual = result.getInt("open_to");
		
		if(openFromActual == 0 && openToActual == 0) { return true; }
		
		if(openFrom >= openFromActual && openTo <= openToActual) {
			return true;
		} else return false;

	}
	
	@Override
	public boolean isAffordable(int dailyBudget, Long spaceId) {
		String sql = "SELECT daily_rate::numeric from space WHERE id = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, spaceId);
		result.next();
		
		double dailyRate = result.getDouble("daily_rate");
		if((int)dailyRate >= dailyBudget) { return true; }
		else return false;
	}

}
