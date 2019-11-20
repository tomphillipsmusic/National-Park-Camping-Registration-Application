package com.techelevator.campground.model;

import java.time.LocalDate;
import java.util.Date;

public class Park {

	private Long parkId;
	private String name;
	private String location;
	private Date dateEstablished;
	private int area;
	private int annualVisitors;
	private String description;

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getDateEstablished() {
		return dateEstablished;
	}

	public void setDateEstablished(Date dateEstablished) {
		this.dateEstablished = dateEstablished;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int getAnnualVisitors() {
		return annualVisitors;
	}

	public void setAnnualVisitors(int annualVisitors) {
		this.annualVisitors = annualVisitors;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
