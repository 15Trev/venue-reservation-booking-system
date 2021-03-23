package com.techelevator.view;

import org.apache.commons.dbcp2.BasicDataSource;


import com.techelevator.reservation.JdbcReservationDao;
import com.techelevator.space.JdbcSpaceDao;
import com.techelevator.venue.JdbcVenueDao;

public class DatabaseReader {
	
	public JdbcReservationDao reserve;
	public JdbcSpaceDao space;
	public JdbcVenueDao venue;
	
	public DatabaseReader() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/excelsior-venues");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		//JdbcTemplate template = new JdbcTemplate(dataSource);
		reserve = new JdbcReservationDao(dataSource);
		space = new JdbcSpaceDao(dataSource);
		venue = new JdbcVenueDao(dataSource);
	}
	
	
	
}
