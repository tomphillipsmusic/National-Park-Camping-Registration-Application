package com.techelevator;

import static org.junit.Assert.*;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.view.LandingMenu;
import com.techelevator.model.jdbc.JDBCParkDAO;

public class JDBCParkDaoIntegrationTest {

	
	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource dataSource;
	private ParkDAO dao;

	/* Before any tests are run, this method initializes the datasource for testing. */
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/* The following line disables autocommit for connections
		 * returned by this DataSource. This allows us to rollback
		 * any changes after each test */
		dataSource.setAutoCommit(false);
		
	}

	/* After all tests have finished running, this method will close the DataSource */
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
@Before
public void setUp() throws Exception {
	dao = new JDBCParkDAO(dataSource);
}
	/* After each test, we rollback any changes that were made to the database so that
	 * everything is clean for the next test */
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	/* This method provides access to the DataSource for subclasses so that
	 * they can instantiate a DAO for testing */
	protected DataSource getDataSource() {
		return dataSource;
	}
	
	//THE FOLLOWING TESTS ARE BASED ON OUR DATABASE INFORMATION ONLY
	
	@Test
	public void test_createListOfAllParks_method_should_return_Acadia() {
		Park allParks[] = dao.createListOfAllParks();
		String expectedResult = "Acadia";
		String actualResult = allParks[0].getName();
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void test_createListOfAllParks_method_length_of_array_should_return_three() {
		Park allParks[] = dao.createListOfAllParks();
		Integer expectedResult = 3;
		Integer actualResult = allParks.length;
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void test_createLandingMenuOptions_method_should_return_Acadia() {
		Park allParks[] = dao.createListOfAllParks();
		Object parkNames[] = LandingMenu.createLandingMenuOptions(allParks);
		Object expectedResult = "Acadia";
		Object actualResult = parkNames[0];
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void test_createLandingMenuOptions_method_length_of_array_should_return_four_including_quit() {
		Park allParks[] = dao.createListOfAllParks();
		Object parkNames[] = LandingMenu.createLandingMenuOptions(allParks);
		Integer expectedResult = 4;
		Integer actualResult = parkNames.length;
		assertEquals(expectedResult, actualResult);
	}
	
	@Test 
	public void test_display_park_information_returns_correct_park() {
		Park allParks[] = dao.createListOfAllParks();
		Park actualPark= dao.displayParkInformation(allParks[0].getName());
		String expectedResult = "Acadia";
		String actualResult = actualPark.getName();
		assertEquals(expectedResult, actualResult);
		expectedResult = "Covering most of Mount Desert Island and other coastal islands, Acadia features the tallest mountain on the Atlantic coast of the United States, granite peaks, ocean shoreline, woodlands, and lakes. There are freshwater, estuary, forest, and intertidal habitats.";
		actualResult = actualPark.getDescription();
		assertEquals(expectedResult, actualResult);
		expectedResult = "47389";
		actualResult = Integer.toString(actualPark.getArea());
		assertEquals(expectedResult, actualResult);

		
		
	}
}
