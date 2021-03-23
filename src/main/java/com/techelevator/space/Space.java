package com.techelevator.space;

public class Space {

	private Long spaceId;
	private Long venueId;
	private String name;
	private boolean isAccessable;
	private int openFrom;
	private int openTo;
	private double dailyRate;
	private int maxOccupancy;
	
	
	
	public Long getSpaceId() {
		return spaceId;
	}
	public Long getVenueId() {
		return venueId;
	}
	public String getName() {
		return name;
	}
	public boolean isAccessible() {
		return isAccessable;
	}
	public int getOpenFrom() {
		return openFrom;
	}
	public int getOpenTo() {
		return openTo;
	}
	public int getDailyRate() {
		return (int)dailyRate;
	}
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	
	
	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}
	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAccessable(boolean isAccessable) {
		this.isAccessable = isAccessable;
	}
	public void setOpenFrom(int openFrom) {
		this.openFrom = openFrom;
	}
	public void setOpenTo(int openTo) {
		this.openTo = openTo;
	}
	public void setDailyRate(double dailyRate) {
		
		this.dailyRate = dailyRate;
	}
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(dailyRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (isAccessable ? 1231 : 1237);
		result = prime * result + maxOccupancy;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + openFrom;
		result = prime * result + openTo;
		result = prime * result + ((spaceId == null) ? 0 : spaceId.hashCode());
		result = prime * result + ((venueId == null) ? 0 : venueId.hashCode());
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
		Space other = (Space) obj;
		if (Double.doubleToLongBits(dailyRate) != Double.doubleToLongBits(other.dailyRate))
			return false;
		if (isAccessable != other.isAccessable)
			return false;
		if (maxOccupancy != other.maxOccupancy)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (openFrom != other.openFrom)
			return false;
		if (openTo != other.openTo)
			return false;
		if (spaceId == null) {
			if (other.spaceId != null)
				return false;
		} else if (!spaceId.equals(other.spaceId))
			return false;
		if (venueId == null) {
			if (other.venueId != null)
				return false;
		} else if (!venueId.equals(other.venueId))
			return false;
		return true;
	}
}
