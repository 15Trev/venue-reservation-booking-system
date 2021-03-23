package com.techelevator.reservation;

import java.sql.Date;
import java.time.LocalDate;

public class Reservation {
	
	private Long reservationId;
	private Long spaceId;
	private int numberOfAttendees;
	private LocalDate startDate;
	private LocalDate endDate;
	private String reservedFor;
	private String spaceName;
	private String venueName;
	private double dailyRate;
	
	
	
	public double getDailyRate() {
		return (int) dailyRate;
	}
	public void setDailyRate(double dailyRate) {
		this.dailyRate = dailyRate;
	}
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	public String getVenueName() {
		return venueName;
	}
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}
	public Long getReservationId() {
		return reservationId;
	}
	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}
	public Long getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(Long spaceId2) {
		this.spaceId = spaceId2;
	}
	public int getNumberOfAttendees() {
		return numberOfAttendees;
	}
	public void setNumberOfAttendees(int numberOfAttendees) {
		this.numberOfAttendees = numberOfAttendees;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate localDate) {
		this.startDate = localDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate localDate) {
		this.endDate = localDate;
	}
	public String getReservedFor() {
		return reservedFor;
	}
	public void setReservedFor(String reservedFor) {
		this.reservedFor = reservedFor;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + numberOfAttendees;
		result = prime * result + ((reservationId == null) ? 0 : reservationId.hashCode());
		result = prime * result + ((reservedFor == null) ? 0 : reservedFor.hashCode());
		result = prime * result + ((spaceId == null) ? 0 : spaceId.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reservation other = (Reservation) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (numberOfAttendees != other.numberOfAttendees)
			return false;
		if (reservationId == null) {
			if (other.reservationId != null)
				return false;
		} else if (!reservationId.equals(other.reservationId))
			return false;
		if (reservedFor == null) {
			if (other.reservedFor != null)
				return false;
		} else if (!reservedFor.equals(other.reservedFor))
			return false;
		if (spaceId == null) {
			if (other.spaceId != null)
				return false;
		} else if (!spaceId.equals(other.spaceId))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}
	
	
	
}
