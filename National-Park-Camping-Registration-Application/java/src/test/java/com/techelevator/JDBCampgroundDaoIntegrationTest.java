package com.techelevator;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;

public class JDBCampgroundDaoIntegrationTest {

	
	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource dataSource;
	private CampgroundDAO dao;
	private ParkDAO parkDao;

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
	dao = new JDBCCampgroundDAO(dataSource);
	parkDao = new JDBCParkDAO(dataSource);
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
	public void test_createListOfAllCampgrounds_method_should_return_all_campgrounds() {
		Campground tempCampground = new Campground();
		Park tempPark = parkDao.displayParkInformation("Acadia");
		List<Campground> tempListOfCampgrounds = dao.createListOfAllCampgrounds(tempPark);
		String expectedResult = "Blackwoods";
		String actualResult = tempListOfCampgrounds.get(0).getName();
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void test_createListOfAllCampgrounds_method_length_should_be_3() {
		Campground tempCampground = new Campground();
		Park tempPark = parkDao.displayParkInformation("Acadia");
		List<Campground> tempListOfCampgrounds = dao.createListOfAllCampgrounds(tempPark);
		Integer expectedResult = 3;
		Integer actualResult = tempListOfCampgrounds.size();
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void test_display_list_of_all_campgrounds_has_BlackWoods() {
		Campground tempCampground = new Campground();
		Park tempPark = parkDao.displayParkInformation("Acadia");
		List<Campground> tempListOfCampgrounds = dao.createListOfAllCampgrounds(tempPark);
		tempListOfCampgrounds = dao.displayListOfAllCampgrounds(tempListOfCampgrounds);
		String expectedResult = "Blackwoods";
		String actualResult = tempListOfCampgrounds.get(0).getName();
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void test_display_list_of_all_campgrounds_has_3_campgrounds() {
		Campground tempCampground = new Campground();
		Park tempPark = parkDao.displayParkInformation("Acadia");
		List<Campground> tempListOfCampgrounds = dao.createListOfAllCampgrounds(tempPark);
		tempListOfCampgrounds = dao.displayListOfAllCampgrounds(tempListOfCampgrounds);
		Integer expectedResult = 3;
		Integer actualResult = tempListOfCampgrounds.size();
		assertEquals(expectedResult, actualResult);
	}


}
