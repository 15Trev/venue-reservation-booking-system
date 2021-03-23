package com.techelevator.venue;

public class Venue {

	private Long venueId;
	private String venueName;
	private String description;
	private Long cityId;
	private String cityName;
	private String stateAbbrev;
	private String stateName;
	private Long categoryId;
	private String categoryName;
	
	
	public Long getVenueId() {
		return venueId;
	}
	public String getVenueName() {
		return venueName;
	}
	public String getDescription() {
		return description;
	}
	public Long getCityId() {
		return cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public String getStateAbbrev() {
		return stateAbbrev;
	}
	public String getStateName() {
		return stateName;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	
	
	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public void setStateAbbrev(String stateAbbrev) {
		this.stateAbbrev = stateAbbrev;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryId == null) ? 0 : categoryId.hashCode());
		result = prime * result + ((categoryName == null) ? 0 : categoryName.hashCode());
		result = prime * result + ((cityId == null) ? 0 : cityId.hashCode());
		result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((stateAbbrev == null) ? 0 : stateAbbrev.hashCode());
		result = prime * result + ((stateName == null) ? 0 : stateName.hashCode());
		result = prime * result + ((venueId == null) ? 0 : venueId.hashCode());
		result = prime * result + ((venueName == null) ? 0 : venueName.hashCode());
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
		Venue other = (Venue) obj;
		if (categoryId == null) {
			if (other.categoryId != null)
				return false;
		} else if (!categoryId.equals(other.categoryId))
			return false;
		if (categoryName == null) {
			if (other.categoryName != null)
				return false;
		} else if (!categoryName.equals(other.categoryName))
			return false;
		if (cityId == null) {
			if (other.cityId != null)
				return false;
		} else if (!cityId.equals(other.cityId))
			return false;
		if (cityName == null) {
			if (other.cityName != null)
				return false;
		} else if (!cityName.equals(other.cityName))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (stateAbbrev == null) {
			if (other.stateAbbrev != null)
				return false;
		} else if (!stateAbbrev.equals(other.stateAbbrev))
			return false;
		if (stateName == null) {
			if (other.stateName != null)
				return false;
		} else if (!stateName.equals(other.stateName))
			return false;
		if (venueId == null) {
			if (other.venueId != null)
				return false;
		} else if (!venueId.equals(other.venueId))
			return false;
		if (venueName == null) {
			if (other.venueName != null)
				return false;
		} else if (!venueName.equals(other.venueName))
			return false;
		return true;
	}
}
