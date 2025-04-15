package br.com.topsys.service.jdbc;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;

@Component
public class TSJdbcTemplate extends TSJdbcRepository {

	
	public TSJdbcTemplate(DataSource dataSource) {
		super.setDataSource(dataSource);
	}
	
	
	
	public static HikariDataSource createTSJdbcTemplate(String url, String userName, String password) {
		return DataSourceBuilder.create()
				.type(HikariDataSource.class)
				.url(url)
				.username(userName)
				.password(password)
				.driverClassName("org.postgresql.Driver")
				.build();
	}
}
