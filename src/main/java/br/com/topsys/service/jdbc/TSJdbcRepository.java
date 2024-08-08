package br.com.topsys.service.jdbc;

import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import br.com.topsys.base.util.TSParseUtil;
import br.com.topsys.base.util.TSUtil;
import br.com.topsys.service.log.TSLog;
import lombok.Data;

@Repository
@Data
public abstract class TSJdbcRepository {

	@Autowired
	private JdbcTemplate dao;

	@Value("${topsys.jdbc.maxrows}")
	private String maxRows;

	private JdbcTemplate getDAO() {
		if (!TSUtil.isEmpty(this.maxRows)) {
			this.dao.setMaxRows(TSParseUtil.stringToInteger(this.maxRows));
		}

		return this.dao;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.getDAO().setDataSource(dataSource);
	}
		

	public Long getSequence(String nome) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT NEXTVAL('");
		builder.append(nome);
		builder.append("')");
		return getDAO().queryForObject(builder.toString(), Long.class);
	}

	public Integer getRowCount(String sql, Object... args) {
		TSLog tsLog = new TSLog(sql, args);
		tsLog.begin();
		try {
			return getDAO().queryForObject(sql, Integer.class, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} finally {
			tsLog.end();
		}

	}

	public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
		TSLog tsLog = new TSLog(sql, args);
		tsLog.begin();
		try {
			return this.getDAO().queryForObject(sql, rowMapper, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} finally {
			tsLog.end();
		}

	}

	public <T> T queryForObject(String sql, Class<T> classe, Object... args) {
		TSLog tsLog = new TSLog(sql, args);
		tsLog.begin();
		try {
			return this.getDAO().queryForObject(sql, classe, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} finally {
			tsLog.end();
		}

	}

	public int update(String sql, Object... args) {
		TSLog tsLog = new TSLog(sql, args);
		tsLog.begin();
		try {
			return this.getDAO().update(sql, args);
		} finally {
			tsLog.end();
		}
	}

	public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
		TSLog tsLog = new TSLog(sql, args);
		tsLog.begin();
		try {
			return this.getDAO().query(sql, rowMapper, args);
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		} finally {
			tsLog.end();
		}
	}

	public <T> List<T> queryForList(String sql, Class<T> classe, Object... args) {
		TSLog tsLog = new TSLog(sql, args);
		tsLog.begin();
		try {
			return this.getDAO().queryForList(sql, classe, args);
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		} finally {
			tsLog.end();
		}
	}

}
