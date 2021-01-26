package br.com.topsys.service.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public abstract class TSJdbcDAO {
	
	@Autowired
	private JdbcTemplate dao;
	
	public JdbcTemplate getDAO() { 
		return dao;
	}
	
	protected Long getSequence(String nome) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT NEXTVAL('");
		builder.append(nome);
		builder.append("')");
		return dao.queryForObject(builder.toString(), Long.class);
	}
	
	

}
