package com.techelevator;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;

public class ReservationDAOIntegrationTest {

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
	public void list_of_availble_sites_returns_size_5_if_dates_available() throws ParseException {

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
	public void returns_empty_list_if_available_dates() throws ParseException {
		Campground testCampground = new Campground();
		testCampground.setCampgroundId(1L);
		List<Site> allSites = siteDAO.returnAllSites(testCampground.getCampgroundId());
		DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		Date fromDate = formatter.parse("10/22/2019");
		Date toDate = formatter.parse("10/26/2019");
		List<Site> availableSites = reservationDAO.listOfAvailableSites(fromDate, toDate, allSites);
		availableSites = siteDAO.displayAvailableSites(availableSites, toDate, fromDate);
		int expectedSize = 0;
		int actualSize = availableSites.size();
		assertEquals(expectedSize, actualSize);

	}

	@Test
	public void insertNewReservation_method_returns_unique_reservation_id() throws ParseException {

		DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		Date fromDate = formatter.parse("10/22/2020");
		Date toDate = formatter.parse("10/26/2020");

		Integer siteId = 1;
		String name = "Flood";

		int actualReservationId = reservationDAO.insertNewReservation(fromDate, toDate, name, siteId);
		String sqlGetReservationId = "SELECT reservation_id FROM reservation WHERE site_id = ? AND "
				+ "name = ? AND from_date = ? AND to_date = ?";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetReservationId, siteId, name, fromDate, toDate);
		results.next();
		int expectedId = results.getInt("reservation_id");

		assertEquals(expectedId, actualReservationId);

	}
}
