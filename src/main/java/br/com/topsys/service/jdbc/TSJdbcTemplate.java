package br.com.topsys.service.jdbc;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

@Component
public final class TSJdbcTemplate extends TSJdbcRepository {

	
	public void setDataSource(DataSource dataSource) {
		super.getDAO().setDataSource(dataSource);
	}
}
