package com.techelevator;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;

public class JDBCSiteDAOIntegrationTest {

	/*
	 * Using this particular implementation of DataSource so that every database
	 * interaction is part of the same database session and hence the same database
	 * transaction
	 */
	private static SingleConnectionDataSource dataSource;
	private SiteDAO siteDAO;
	private ReservationDAO reservationDAO;

	/*
	 * Before any tests are run, this method initializes the datasource for testing.
	 */
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/*
		 * The following line disables autocommit for connections returned by this
		 * DataSource. This allows us to rollback any changes after each test
		 */
		dataSource.setAutoCommit(false);

	}

	/*
	 * After all tests have finished running, this method will close the DataSource
	 */
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	@Before
	public void setUp() throws Exception {
		siteDAO = new JDBCSiteDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);

	}

	/*
	 * After each test, we rollback any changes that were made to the database so
	 * that everything is clean for the next test
	 */
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	/*
	 * This method provides access to the DataSource for subclasses so that they can
	 * instantiate a DAO for testing
	 */
	protected DataSource getDataSource() {
		return dataSource;
	}

	@Test
	public void return_all_sites_returns_correct_size() {
		Campground testCampground = new Campground();
		testCampground.setCampgroundId(1L);
		List<Site> allSites = siteDAO.returnAllSites(testCampground.getCampgroundId());
		int expectedSize = 276;
		int actualSize = allSites.size();
		assertEquals(expectedSize, actualSize);
	}

	@Test
	public void list_of_all_sites_index_1_has_site_number_1() {
		Campground testCampground = new Campground();
		testCampground.setCampgroundId(1L);
		List<Site> allSites = siteDAO.returnAllSites(testCampground.getCampgroundId());
		int expectedSiteNumber = 2;
		int actualSiteNumber = allSites.get(1).getSiteNumber();
		assertEquals(expectedSiteNumber, actualSiteNumber);
	}



	@Test
	public void display_Available_Sites_Returns_5() throws ParseException {
		Campground testCampground = new Campground();
		testCampground.setCampgroundId(1L);
		List<Site> allSites = siteDAO.returnAllSites(testCampground.getCampgroundId());
		DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		Date fromDate = formatter.parse("06/01/2020");
		Date toDate = formatter.parse("06/01/2020");
		List<Site> availableSites = reservationDAO.listOfAvailableSites(fromDate, toDate, allSites);
		availableSites = siteDAO.displayAvailableSites(availableSites, toDate, fromDate);
		int expectedSize = 5;
		int actualSize = availableSites.size();
		assertEquals(expectedSize, actualSize);
	}
	
	@Test
	public void calculate_cost_for_two_day_booking_thirty_dollar_rate_returns_60() throws ParseException {
		Campground testCampground = new Campground();
		testCampground.setCampgroundId(2L);
		testCampground.setParkId(1L);
		testCampground.setName("Blackwoods");
		testCampground.setDailyFee(BigDecimal.valueOf(30));
		List<Site> allSites = siteDAO.returnAllSites(testCampground.getCampgroundId());
		DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		Date fromDate = formatter.parse("06/01/2020");
		Date toDate = formatter.parse("06/03/2020");
		String expectedCost = "$60.00";
		String actualCost = siteDAO.calculateCost(toDate, fromDate, allSites.get(1));
		assertEquals(expectedCost, actualCost);
	}

}
