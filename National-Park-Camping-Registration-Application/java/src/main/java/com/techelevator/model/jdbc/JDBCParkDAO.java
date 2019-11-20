package com.techelevator.model.jdbc;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO{


	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Park[] createListOfAllParks() {
		List<Park> allParks = new ArrayList<>();
		String sqlSelectEverythingFromParks = "SELECT * FROM park";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectEverythingFromParks);
		while (results.next()) {
			Park tempPark = new Park();
			tempPark.setParkId(results.getLong("park_id"));
			tempPark.setName(results.getString("name"));
			tempPark.setLocation(results.getString("location"));
			tempPark.setDateEstablished(results.getDate("establish_date"));
			tempPark.setArea(results.getInt("area"));
			tempPark.setAnnualVisitors(results.getInt("visitors"));
			tempPark.setDescription(results.getString("description"));
			allParks.add(tempPark);
		}
		
		Park allParksArray[] = new Park[allParks.size()];
		allParksArray = allParks.toArray(allParksArray);
		
		return allParksArray;
	}

	public Park displayParkInformation(String choice) {
		
		String sqlSelectChosenParkInfo = "SELECT * from park WHERE name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectChosenParkInfo, choice);
		Park tempPark = new Park();
		
		while (results.next()) {
			
			tempPark.setParkId(results.getLong("park_id"));
			tempPark.setName(results.getString("name"));
			tempPark.setLocation(results.getString("location"));
			tempPark.setDateEstablished(results.getDate("establish_date"));
			tempPark.setArea(results.getInt("area"));
			tempPark.setAnnualVisitors(results.getInt("visitors"));
			tempPark.setDescription(results.getString("description"));
			
			String location = "Location: ";
			
			String pattern = "MM/dd/yyyy";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String formattedDate = simpleDateFormat.format(tempPark.getDateEstablished());
			
			
			String formattedArea = String.format("%,d", tempPark.getArea()) + " sq km";
			String formattedVisitors = String.format("%,d", tempPark.getAnnualVisitors());
			
			
			System.out.println("\n" + tempPark.getName() + " National Park\n");
			System.out.printf("%-20s%30s\n", "Location: ", tempPark.getLocation());
			System.out.printf("%-20s%30s\n", "Established: ", formattedDate);
			System.out.printf("%-20s%30s\n", "Area: ", formattedArea);
			System.out.printf("%-20s%30s\n", "Annual Visitors: ", formattedVisitors);
			System.out.println("\n" + tempPark.getDescription());
			
		}
		
		
		
		return tempPark;
	}
	
	
}
