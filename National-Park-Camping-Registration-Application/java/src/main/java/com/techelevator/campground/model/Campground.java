package com.techelevator.campground.model;

import java.math.BigDecimal;

public class Campground {

	private Long campgroundId;
	private Long parkId;
	private String name;
	private int monthOpen;
	private int monthClose;
	private BigDecimal dailyFee;

	public Long getCampgroundId() {
		return campgroundId;
	}

	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}

	public Long getParkId() {
		return parkId;
	}

	public void setParkId(Long parkId) {
		this.parkId = parkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMonthOpen() {
		return monthOpen;
	}

	public void setMonthOpen(int monthOpen) {
		this.monthOpen = monthOpen;
	}

	public int getMonthClose() {
		return monthClose;
	}

	public void setMonthClose(int monthClose) {
		this.monthClose = monthClose;
	}

	public BigDecimal getDailyFee() {
		return dailyFee;
	}

	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}

	public String numberToMonthConversion(int monthNumber) {
		String months[] = {"January", "February", "March", "April", "May", 
						   "June", "July", "August", "September", "October", 
						   "November", "December"};
		return months[monthNumber - 1];
	}
}
