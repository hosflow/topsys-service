package br.com.topsys.service.jdbc;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class TSJdbcTemplate extends TSJdbcRepository {

	private DataSource dataSource;
	
	public TSJdbcTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@PostConstruct
	protected void init() {
		super.setDataSource(dataSource);
	}
	
	
	public static DataSource createDataSource(String url, String userName, String password) {
		return DataSourceBuilder.create()
				.url(url)
				.username(userName)
				.password(password)
				.driverClassName("org.postgresql.Driver")
				.build();
		 
		

	}
}
