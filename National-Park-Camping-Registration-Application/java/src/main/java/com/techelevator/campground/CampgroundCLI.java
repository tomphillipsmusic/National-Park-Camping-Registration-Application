package com.techelevator.campground;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;
import com.techelevator.campground.view.CampgroundInfoMenu;
import com.techelevator.campground.view.LandingMenu;
import com.techelevator.campground.view.Menu;
import com.techelevator.campground.view.ParkInfoMenu;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;

public class CampgroundCLI {

	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO; 
	private ReservationDAO reservationDAO;

	public static void main(String[] args) throws ParseException {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		parkDAO = new JDBCParkDAO(datasource);
		campgroundDAO = new JDBCCampgroundDAO(datasource);
		siteDAO = new JDBCSiteDAO(datasource);
		reservationDAO = new JDBCReservationDAO(datasource);

	}

	public void run() throws ParseException {
		Menu menu = new Menu(System.in, System.out);
		Park allParks[] = parkDAO.createListOfAllParks();
		Object parkNames[] = LandingMenu.createLandingMenuOptions(allParks);
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(parkNames);
			if (choice.equals("Quit")) {
				System.exit(0);
			} else {
				parkInfoMenu(choice, menu);

			}

		}
	}

	public void parkInfoMenu(String choice, Menu menu) throws ParseException {
		while (true) {
			Park currentPark = parkDAO.displayParkInformation(choice);

			String[] parkInfoMenuOptions = ParkInfoMenu.createParkInfoMenuOptions();
			choice = (String) menu.getChoiceFromOptions(parkInfoMenuOptions);
			if (choice.equals("Return to Previous Screen")) {
				break;

			} else {
				displayCampGroundMenu(currentPark, menu);
			}

		}
	}

	public void displayCampGroundMenu(Park currentPark, Menu menu) throws ParseException {
		while (true) {
			List<Campground> menuListOfCampgrounds = campgroundDAO.createListOfAllCampgrounds(currentPark);
			campgroundDAO.displayListOfAllCampgrounds(menuListOfCampgrounds);
			String[] campgroundInfoMenuOptions = CampgroundInfoMenu.createCampgroundInfoMenuOptions();
			String choice = (String) menu.getChoiceFromOptions(campgroundInfoMenuOptions);

			if (choice.equals("Search For Available Reservations")) {
				searchForReservation(currentPark);

			}
			if (choice.equals("Return to Previous Screen")) {
				break;
			}
		}

	}

	public void searchForReservation(Park currentPark) throws ParseException {
		while (true) {

			List<Campground> menuListOfCampgrounds = campgroundDAO.createListOfAllCampgrounds(currentPark);
			campgroundDAO.displayListOfAllCampgrounds(menuListOfCampgrounds);

			Scanner userInput = new Scanner(System.in);
			System.out.println("\nPlease select the number of the campground (enter 0 to cancel): ");
			String campgroundSelection = userInput.nextLine();
			if (campgroundSelection.equals("0")) {
				break;
			}
			int selectedCampground = Integer.parseInt(campgroundSelection) - 1;

			System.out.println("\nEnter Arrival Date __/__/____: ");
			try {
				String fromDateSelection = userInput.nextLine();
				Date fromDate = convertStringToDate(fromDateSelection);
				System.out.println("\nEnter Departure Date __/__/____: ");
				String toDateSelection = userInput.nextLine();
				Date toDate = convertStringToDate(toDateSelection);
				Long selectedCampgroundId = menuListOfCampgrounds.get(selectedCampground).getCampgroundId();
				List<Site> allSitesAtCurrentPark = siteDAO.returnAllSites(selectedCampgroundId);
				List<Site> availableSites = reservationDAO.listOfAvailableSites(fromDate, toDate,
						allSitesAtCurrentPark);
				if (availableSites.size() == 0) {
					System.out.println(
							"\nSorry, no available reservation slots. Would you like to try different dates? (Y or N)");
					String selectYN = userInput.nextLine();
					selectYN = selectYN.toLowerCase();
					selectYN = selectYN.trim();
					if (selectYN.equals("n")) {
						break;
					}

				} else {

					bookReservation(toDate, fromDate, availableSites);
				}
			} catch (ParseException e) {
				System.out.println("You must enter valid date format (MM/DD/YYYY)");
			}

		}
	}

	public void bookReservation(Date toDate, Date fromDate, List<Site> availableSites) {
		while (true) {
			Scanner userInput = new Scanner(System.in);

			siteDAO.displayAvailableSites(availableSites, toDate, fromDate);
			System.out.println("\nWhich site should be reserved? (enter 0 to cancel): ");
			String userEnterSite = userInput.nextLine();

			if (userEnterSite.equals("0")) {
				break;
			}
			Integer reservationSite = Integer.parseInt(userEnterSite);
			System.out.println("What name should the reservation be made under?: ");
			String userEnterName = userInput.nextLine();
			int newReservationId = reservationDAO.insertNewReservation(fromDate, toDate, userEnterName,
					reservationSite);
			System.out.println("\nThe reservation has been made and the Confirmation ID is " + newReservationId + " .");
			System.out.println("\nThank you for supporting our national parks! Goodbye!");
			System.exit(0);

		}

	}

	public Date convertStringToDate(String date) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date convertedDate = formatter.parse(date);
		return convertedDate;
	}
}
