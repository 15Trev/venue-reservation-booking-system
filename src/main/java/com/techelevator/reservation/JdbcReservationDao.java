package com.techelevator.reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcReservationDao implements ReservationDao{

	private JdbcTemplate jdbc;
	
	public JdbcReservationDao(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Long save(Reservation reservation) {
		String sql = "INSERT INTO reservation VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING reservation_id";
		SqlRowSet row = jdbc.queryForRowSet(sql,
				reservation.getSpaceId(), 
				reservation.getNumberOfAttendees(), 
				reservation.getStartDate(), 
				reservation.getEndDate(), 
				reservation.getReservedFor());
		if(row.next()) {
			reservation.setReservationId(row.getLong("reservation_id"));
		}
		return row.getLong("reservation_id");
	}

	@Override
	public void update(Reservation reservation) {
		String sql = "UPDATE reservation SET space_id = ?, number_of_attendees = ?, start_date = ?, end_date = ?, reserved_for = ?";
		jdbc.update(sql,
				reservation.getSpaceId(),
				reservation.getNumberOfAttendees(), 
				reservation.getStartDate(), 
				reservation.getEndDate(), 
				reservation.getReservedFor());
	}

	@Override
	public void delete(Long id) {
		String sql = "DELETE FROM reservation WHERE reservation_id = ?";
		jdbc.update(sql, id);
	}
	
	@Override
	public List<Reservation> findAllReservations() {
		String sql = "SELECT reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for FROM reservation";
		SqlRowSet result = jdbc.queryForRowSet(sql);
		List<Reservation> list = new ArrayList<Reservation>();
		while(result.next()) {
			list.add ( mapResultToReservation(result) );
		}
		return list;
	}
	@Override
	public Reservation findReservationById(Long reservationId) {
		String sql = "SELECT reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for FROM reservation WHERE reservation_id = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, reservationId);
		if(result.next()) {
			Reservation reservation = mapResultToReservation(result);
			return reservation;
		}
		return null;
	}
	
	@Override
	public List<Reservation> findReservationByName(String reservationName, Long venueId) {
		List<Reservation> reservations = new ArrayList<Reservation>();
		String sql = "SELECT reservation_id, space.name AS space_name, venue.name AS venue_name, number_of_attendees, start_date, end_date, reserved_for, space.daily_rate::numeric AS daily_rate FROM reservation r "
				   + "JOIN space ON r.space_id = space.id "
				   + "JOIN venue ON space.venue_id = venue.id "
				   + "WHERE reserved_for = ? AND venue_id = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, reservationName, venueId);
		while(result.next()) {
			Reservation reservation = new Reservation();
			reservation.setReservationId(result.getLong("reservation_id"));
			reservation.setSpaceName(result.getString("space_name"));
			reservation.setVenueName(result.getString("venue_name"));
			reservation.setNumberOfAttendees(result.getInt("number_of_attendees"));
			reservation.setStartDate(LocalDate.parse(result.getString("start_date")));
			reservation.setEndDate(LocalDate.parse(result.getString("end_date")));
			reservation.setReservedFor(result.getString("reserved_for"));
			reservation.setDailyRate(result.getDouble("daily_rate"));
			reservations.add(reservation);
		}
		return reservations;
	}
	
	@Override
	public List<Reservation> findReservationBySpaceId(Long spaceId) {
		List<Reservation> reservationList = new ArrayList<Reservation>();
		String sql = "SELECT reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for FROM reservation WHERE space_id = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, spaceId);
		while(result.next()) {
			Reservation reservation = mapResultToReservation(result);
			reservationList.add(reservation);
		}
		return reservationList;
	}

	@Override
	public List<Reservation> findReservationByStartDate(LocalDate localDate) {
		List<Reservation> reservationList = new ArrayList<Reservation>();
		String sql = "SELECT r.reservation_id, r.space_id, v.name AS venue_name, s.name AS space_name, number_of_attendees, start_date, end_date, reserved_for FROM reservation r "
				+ "JOIN space s ON s.id = r.space_id "
				+ "JOIN venue v ON v.id = s.venue_id "
				+ "WHERE start_date = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, localDate);
		while(result.next()) {
			Reservation reservation = mapResultToReservation(result);
			reservation.setVenueName(result.getString("venue_name"));
			reservation.setSpaceName(result.getString("space_name"));
			reservationList.add(reservation);
		}
		return reservationList;
	}

	@Override
	public List<Reservation> findReservationByReservedFor(String reservedFor) {
		List<Reservation> reservationList = new ArrayList<Reservation>();

		String sql = "SELECT reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for FROM reservation WHERE reserved_for = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, reservedFor);
		while(result.next()) {
			Reservation reservation = mapResultToReservation(result);
			reservationList.add(reservation);
		}
		return reservationList;
	}
	
	private Reservation mapResultToReservation(SqlRowSet result) {
		Reservation newReservation = new Reservation();
		newReservation.setReservationId(result.getLong("reservation_id"));
		newReservation.setSpaceId(result.getLong("space_id"));
		newReservation.setNumberOfAttendees(result.getInt("number_of_attendees"));
		newReservation.setStartDate(LocalDate.parse(result.getString("start_date")));
		newReservation.setEndDate(LocalDate.parse(result.getString("end_date")));
		newReservation.setReservedFor(result.getString("reserved_for"));
		return newReservation;
	}
	
	@Override
	public boolean isReserved(LocalDate date, int duration, Long spaceId) {
		LocalDate startDate = date;
		LocalDate endDate = date.plusDays(duration);
		
		String sql = "SELECT reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for FROM reservation WHERE space_id = ?";
		SqlRowSet result = jdbc.queryForRowSet(sql, spaceId);

		boolean isReserved = false;
		
		while(result.next()) {
			LocalDate reservationStart = result.getDate("start_date").toLocalDate();
			LocalDate reservationEnd = result.getDate("end_date").toLocalDate();
			
	//		It returns 0 if both the dates are equal.
	//		It returns positive value if â€œthis dateâ€� is greater than the otherDate.
	//		It returns negative value if â€œthis dateâ€� is less than the otherDate.
			
			if ( startDate.compareTo(reservationStart) > 0 && startDate.compareTo(reservationEnd) > 0 ) { isReserved = false; }
			else if ( startDate.compareTo(reservationStart) < 0 && endDate.compareTo(reservationStart) < 0 ) { isReserved = false; }
			else isReserved = true;
		}
		return isReserved;
	}
}
