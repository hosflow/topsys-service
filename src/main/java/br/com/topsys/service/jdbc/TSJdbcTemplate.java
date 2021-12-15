package br.com.topsys.service.jdbc;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

@Component
public class TSJdbcTemplate extends TSJdbcRepository {

	
	public TSJdbcTemplate() {
		
	}
	
	public TSJdbcTemplate(DataSource dataSource) {
		super();
		super.getDAO().setDataSource(dataSource);
	}
	
	public void setDataSource(DataSource dataSource) {
		super.getDAO().setDataSource(dataSource);
	}
}
