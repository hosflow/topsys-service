package br.com.topsys.service.jdbc;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

@Component
public class TSJdbcTemplate extends TSJdbcRepository {

	
	public TSJdbcTemplate() {
		
	}
	
	public void setDataSource(DataSource dataSource) {
		super.getDAO().setDataSource(dataSource);
	}
}
