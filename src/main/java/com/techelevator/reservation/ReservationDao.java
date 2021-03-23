package com.techelevator.reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDao {

	public Long save(Reservation reservation);
	
	public void update(Reservation reservation);
	
	public void delete(Long reservationId);
	
	public Reservation findReservationById(Long reservationId);
	
	public List<Reservation> findReservationBySpaceId(Long spaceId);
	
	public List<Reservation> findReservationByStartDate(LocalDate startDate);
	
	public List<Reservation> findReservationByReservedFor(String reservedFor);

	List<Reservation> findAllReservations();

	boolean isReserved(LocalDate date, int duration, Long spaceId);

	List<Reservation> findReservationByName(String reservationName, Long venueId);
	
}
