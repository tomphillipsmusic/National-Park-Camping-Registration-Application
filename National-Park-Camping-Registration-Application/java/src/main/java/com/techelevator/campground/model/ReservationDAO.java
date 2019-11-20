package com.techelevator.campground.model;

import java.util.Date;
import java.util.List;

public interface ReservationDAO {

	List<Site> listOfAvailableSites (Date fromDate, Date toDate, List<Site> listOfAllSites);
	Integer insertNewReservation (Date fromDate, Date toDate, String name, Integer siteId);
		
	}
	

