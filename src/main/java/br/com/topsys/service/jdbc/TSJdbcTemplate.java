package br.com.topsys.service.jdbc;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

@Component
public class TSJdbcTemplate extends TSJdbcRepository {

	private DataSource dataSource;
	
	public TSJdbcTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@jakarta.annotation.PostConstruct
	public void init() {
		super.getDAO().setDataSource(dataSource);
	}
	
	public void setDataSource(DataSource dataSource) {
		super.getDAO().setDataSource(dataSource);
	}
}
