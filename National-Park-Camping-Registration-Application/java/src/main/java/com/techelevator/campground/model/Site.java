package com.techelevator.campground.model;

import java.math.BigDecimal;

public class Site {

	private Long siteId;
	private Long campgroundId;
	private int siteNumber;
	private int maxOccupancy;
	private boolean accessible;
	private int maxRvLength;
	private boolean utilities;

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public Long getCampgroundId() {
		return campgroundId;
	}

	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}

	public int getSiteNumber() {
		return siteNumber;
	}

	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}

	public int getMaxOccupancy() {
		return maxOccupancy;
	}

	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}

	public String getAccessible() {
		if (this.accessible) {
			return "Yes";
		}
		return "No";
	}

	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}

	public String getMaxRvLength() {
		if (this.maxRvLength == 0) {
			return "N/A";
		}
		return this.getMaxRvLength().toString();

	}

	public void setMaxRvLength(int maxRvLength) {
		this.maxRvLength = maxRvLength;
	}

	public String isUtilities() {
		if (this.utilities) {
			return "Yes";
		}
		return "N/A";
	}

	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}

}
