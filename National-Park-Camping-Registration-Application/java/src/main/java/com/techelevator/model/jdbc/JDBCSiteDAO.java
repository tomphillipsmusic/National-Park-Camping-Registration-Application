package com.techelevator.model.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Site> returnAllSites(Long campgroundId) {
		String sqlGetAllSitesFromSelectedCampground = "SELECT * FROM site WHERE campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllSitesFromSelectedCampground, campgroundId);
		List<Site> listOfAllSites = new ArrayList<>();
		while (results.next()) {
			Site tempSite = new Site();
			tempSite.setSiteId(results.getLong("site_id"));
			tempSite.setCampgroundId(results.getLong("campground_id"));
			tempSite.setSiteNumber(results.getInt("site_number"));
			tempSite.setMaxOccupancy(results.getInt("max_occupancy"));
			tempSite.setAccessible(results.getBoolean("accessible"));
			tempSite.setMaxRvLength(results.getInt("max_rv_length"));
			tempSite.setUtilities(results.getBoolean("utilities"));
			listOfAllSites.add(tempSite);

		}

		return listOfAllSites;
	}

	@Override
	public List<Site> displayAvailableSites(List<Site> availableSites, Date toDate, Date fromDate) {

		
		System.out.printf("%-20s%-20s%-20s%-20s%-20s%-20s\n", "Site No", "Max Occup", "Accessible", "Max RV Length",
				"Utility", "Cost");
		
		
		for (Site site : availableSites) {
			
			System.out.printf("#%-19d%-20s%-20s%-20s%-20s%-20s\n", site.getSiteId(), site.getMaxOccupancy(),
					site.getAccessible(), site.getMaxRvLength(), site.isUtilities(),
					calculateCost(toDate, fromDate, site));
		}

		return availableSites;
	}

	@Override
	public String calculateCost(Date toDate, Date fromDate, Site site) {
		String sqlSelectCampground = "SELECT * from campground WHERE campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectCampground, site.getCampgroundId());
		Campground campground = new Campground();
		BigDecimal totalCost = new BigDecimal("0");
		while (results.next()){
		campground.setDailyFee(results.getBigDecimal("daily_fee"));
		}
		String sqlSubtractDates = "SELECT ?::date - ?::date";
		results = jdbcTemplate.queryForRowSet(sqlSubtractDates, toDate, fromDate);
		while (results.next()) {
		String totalDaysBooked = results.getString("?column?");
		int totalDaysBookedToNum = Integer.parseInt(totalDaysBooked);
		totalCost = campground.getDailyFee().multiply(BigDecimal.valueOf(totalDaysBookedToNum).setScale(1));
		
		}
		String formattedCost = "$" + totalCost.toString();

		return formattedCost;
	}

}
