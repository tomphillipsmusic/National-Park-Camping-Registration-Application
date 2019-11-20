package com.techelevator.campground.model;

import java.util.Date;
import java.util.List;

public interface SiteDAO {

	List<Site> returnAllSites (Long campgroundId);
	List<Site> displayAvailableSites(List<Site> availableSites, Date toDate, Date fromDate);
	String calculateCost (Date toDate, Date fromDate, Site site);
	
}
