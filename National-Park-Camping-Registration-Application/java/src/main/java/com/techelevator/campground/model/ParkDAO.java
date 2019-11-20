package com.techelevator.campground.model;

public interface ParkDAO {

	Park [] createListOfAllParks();
	Park displayParkInformation(String choice);
}
