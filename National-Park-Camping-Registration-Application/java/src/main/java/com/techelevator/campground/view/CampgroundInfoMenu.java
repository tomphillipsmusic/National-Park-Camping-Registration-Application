package com.techelevator.campground.view;

public class CampgroundInfoMenu {
	
	private final static String CAMPGROUND_MENU_OPTION_SEARCH_FOR_RESERVATION = "Search For Available Reservations";
	private final static String CAMPGROUND_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Screen";
	private final static String [] CAMPGROUND_MENU_OPTIONS = {CAMPGROUND_MENU_OPTION_SEARCH_FOR_RESERVATION, 
			                       CAMPGROUND_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN };
	

	public static String[] createCampgroundInfoMenuOptions() {
		return CAMPGROUND_MENU_OPTIONS;
	}
}
