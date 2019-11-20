package com.techelevator.model.jdbc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.Site;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Site> listOfAvailableSites(Date fromDate, Date toDate, List<Site> listOfAllSites) {
		List<Site> availableSites = new ArrayList<>();
		for (Site site : listOfAllSites) {
			String sqlQueryForSiteAvailability = "SELECT * FROM reservation WHERE site_id = ? AND ((to_date BETWEEN ? AND ?) OR "
					+ "(from_date BETWEEN ? AND ?))";
			SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQueryForSiteAvailability, site.getSiteId(), fromDate,
					toDate, fromDate, toDate);

			int numberOfConflicts = 0;
			while (results.next()) {
				numberOfConflicts++;
			}
			if (numberOfConflicts == 0) {
				availableSites.add(site);
				
			}
			if (availableSites.size() == 5) {
				break;
			}
			
		}
		
		return availableSites;
	}

	@Override
	public Integer insertNewReservation(Date fromDate, Date toDate, String name, Integer siteId) {
		
		
		Date todaysDate = new Date();
		todaysDate = Calendar.getInstance().getTime();
		SimpleDateFormat onlyDate = new SimpleDateFormat("yyyy-MM-dd");
		
		String sqlNewReservation = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date)"
		                         + "VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlNewReservation, siteId, name, fromDate, toDate, todaysDate);
		
		String sqlGetReservationId = "SELECT reservation_id FROM reservation WHERE site_id = ? AND "
		+ "name = ? AND from_date = ? AND to_date = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetReservationId, siteId, name, fromDate, toDate);
		results.next();
		Integer reservationId = results.getInt("reservation_id");
		
		return reservationId;
	}
	
	
	

}
 