package com.techelevator.model.jdbc;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Campground> createListOfAllCampgrounds(Park park) {
		List<Campground> chosenCampgrounds = new ArrayList<>();
		String sqlSelectCampgroundsFromChosenPark = "SELECT B.name, B.campground_id, B.park_id,"
				+ "B.open_from_mm, B.open_to_mm, B.daily_fee FROM park A JOIN "
				+ "campground B ON A.park_id = B.park_id WHERE A.name = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectCampgroundsFromChosenPark, park.getName());

		while (results.next()) {
			Campground tempCampground = new Campground();
			tempCampground.setCampgroundId(results.getLong("campground_id"));
			tempCampground.setParkId(results.getLong("park_id"));
			tempCampground.setName(results.getString("name"));
			tempCampground.setMonthOpen(results.getInt("open_from_mm"));
			tempCampground.setMonthClose(results.getInt("open_to_mm"));
			tempCampground.setDailyFee(results.getBigDecimal("daily_fee"));
			chosenCampgrounds.add(tempCampground);

		}
		return chosenCampgrounds;
	}

	@Override
	public List<Campground> displayListOfAllCampgrounds(List<Campground> campgroundList) {
		int campgroundCounter = 1;
		System.out.printf("\n%-13s%-39s%-30s%-20s%-20s\n", "Number: ", "Name: ", "Open: ", "Close: ", "Daily Fee: ");
		for (Campground tempCampgrounds : campgroundList) {
			String openMonth = tempCampgrounds.numberToMonthConversion(tempCampgrounds.getMonthOpen());
			String closeMonth = tempCampgrounds.numberToMonthConversion(tempCampgrounds.getMonthClose());

			String dailyFeeString = "$" + tempCampgrounds.getDailyFee().setScale(2).toString();
			System.out.printf("#%-13d%-39s%-30s%-20s%-20s\n", campgroundCounter,
					tempCampgrounds.getName(), openMonth, closeMonth, dailyFeeString);
			campgroundCounter++;
		}
		return campgroundList;
	}

}
