package br.com.topsys.service.jdbc;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

@Component
public class TSJdbcTemplate extends TSJdbcRepository {

	
	public TSJdbcTemplate(DataSource dataSource) {
		super.setDataSource(dataSource);
	}
	
	
	
	public static TSJdbcTemplate createTSJdbcTemplate(String url, String userName, String password) {
		DataSource dataSource = DataSourceBuilder.create()
				.url(url)
				.username(userName)
				.password(password)
				.driverClassName("org.postgresql.Driver")
				.build();
		 return new TSJdbcTemplate(dataSource);
		

	}
}
