package com.techelevator.campground.view;

import com.techelevator.campground.model.Park;

public class LandingMenu {

	private static String[] LANDING_MENU_OPTIONS;
	
	public static Object[] createLandingMenuOptions(Park allParks[]) {
		int lengthOfMenu = allParks.length + 1;
		LANDING_MENU_OPTIONS = new String [lengthOfMenu];
		for (int i = 0; i < LANDING_MENU_OPTIONS.length - 1; i++) {
			LANDING_MENU_OPTIONS[i] = allParks[i].getName();
		}
		LANDING_MENU_OPTIONS[LANDING_MENU_OPTIONS.length - 1] = "Quit";
		
		return LANDING_MENU_OPTIONS;
	}
}
