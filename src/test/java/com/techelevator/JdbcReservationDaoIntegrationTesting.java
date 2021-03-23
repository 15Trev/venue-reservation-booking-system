package com.techelevator;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.reservation.JdbcReservationDao;
import com.techelevator.reservation.Reservation;
import com.techelevator.space.Space;
import com.techelevator.venue.Venue;

public class JdbcReservationDaoIntegrationTesting extends DAOIntegrationTest {

	private static DataSource dataSource = DAOIntegrationTest.getDataSource();
	private JdbcReservationDao reservationDao;
	private JdbcTemplate jdbc = new JdbcTemplate(dataSource);
	private Long spaceId;
	private Reservation reservation;
	
	@Before
	public void setup() {
		reservationDao = new JdbcReservationDao(dataSource);
	}
	
	@Test
	public void reservation_save_reservation() {
		int oldListAmount = reservationDao.findAllReservations().size();
		
		makeAll();
		reservationDao.save( makeReservation() );
		
		int newListAmount = reservationDao.findAllReservations().size();
		Assert.assertEquals(oldListAmount + 1, newListAmount);
	}
	@Test
	public void reservation_update() {
		makeAll();
		makeReservation();
		int firstCount = reservation.getNumberOfAttendees();
		reservationDao.save( reservation );
		
		reservation.setNumberOfAttendees(300);
		reservationDao.update(reservation);
		int secondCount = reservationDao.findReservationById(reservation.getReservationId()).getNumberOfAttendees();
		
		Assert.assertEquals(firstCount +100, secondCount);
	}
	@Test
	public void reservation_delete() {
		makeAll();
		Reservation reservation = makeReservation();
		reservationDao.save( reservation );
		int firstCount = reservationDao.findAllReservations().size();
		reservationDao.delete(reservation.getReservationId());
		int secondCount = reservationDao.findAllReservations().size();
		
		Assert.assertEquals(firstCount-1, secondCount);
		
	}
	@Test
	public void reservation_find_Reservation_By_Id() {
		makeAll();
		reservationDao.save( makeReservation() );
		Assert.assertEquals(reservation, reservationDao.findReservationById(reservation.getReservationId()));
	}
	@Test
	public void reservation_find_Reservation_By_Space_Id() {
		makeAll();
		reservationDao.save( makeReservation() );
		Reservation result = reservationDao.findReservationBySpaceId(spaceId).get(0);
		Assert.assertEquals(reservation, result);
	}
	@Test
	public void find_Reservation_By_Start_Date() {
		makeAll();
		reservationDao.save( makeReservation() );
		List<Reservation> list = reservationDao.findReservationByStartDate(reservation.getStartDate());
		Assert.assertEquals(reservation, list.get(list.size()-1) );
	}
	@Test
	public void find_Reservation_By_Reserved_For() {
		makeAll();
		reservationDao.save( makeReservation() );
		List<Reservation> list = reservationDao.findReservationByReservedFor(reservation.getReservedFor());
		Assert.assertEquals(reservation, list.get(list.size()-1) );
		
	}
	
	private Reservation makeReservation() {
		Reservation newReservation = new Reservation();
		newReservation.setSpaceId(spaceId);
		newReservation.setNumberOfAttendees(200);
		newReservation.setStartDate(LocalDate.parse("2021-02-20"));
		newReservation.setEndDate(LocalDate.parse("2021-02-28"));
		newReservation.setReservedFor("Test Family");
		this.reservation =  newReservation;
		return newReservation;
	}
	
	private Space makeSpace() {
		Space newSpace = new Space();
		newSpace.setName("Test Space");
		newSpace.setAccessable(false);
		newSpace.setOpenFrom(1);
		newSpace.setOpenTo(3);
		newSpace.setDailyRate(4000);
		newSpace.setMaxOccupancy(201);
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
		Long venueId = row.getLong("id");
		
		Space space = makeSpace();
		sql = "INSERT INTO space VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
		row = jdbc.queryForRowSet(sql, venueId, space.getName(), space.isAccessible(), space.getOpenFrom(), space.getOpenTo(), space.getDailyRate(), space.getMaxOccupancy());
		row.next();
		this.spaceId = row.getLong("id");
	}
}