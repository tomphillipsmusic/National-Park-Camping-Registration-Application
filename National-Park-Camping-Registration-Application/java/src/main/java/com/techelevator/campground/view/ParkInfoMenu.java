package com.techelevator.campground.view;

public class ParkInfoMenu {
	private final static String PARK_INFO_MENU_OPTION_VIEW_CAMPGROUNDS = "View Campgrounds";
	private final static String PARK_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Screen";
	private final static String [] PARK_MENU_OPTIONS = {PARK_INFO_MENU_OPTION_VIEW_CAMPGROUNDS,
														PARK_MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN};
	

	public static String[] createParkInfoMenuOptions() {
		return PARK_MENU_OPTIONS;
	}
}
