package br.com.topsys.service.jdbc;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

@Component
public class TSJdbcTemplate extends TSJdbcRepository {

	private DataSource dataSource;
	
	public TSJdbcTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@PostConstruct
	protected void init() {
		super.getDAO().setDataSource(dataSource);
	}
	
	protected void setDataSource(DataSource dataSource) {
		super.getDAO().setDataSource(dataSource);
	}
}
