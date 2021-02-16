package br.com.topsys.service.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import br.com.topsys.base.util.TSParseUtil;
import br.com.topsys.base.util.TSUtil;
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

	protected Long getSequence(String nome) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT NEXTVAL('");
		builder.append(nome);
		builder.append("')");
		return getDAO().queryForObject(builder.toString(), Long.class);
	}

	public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
		try {
			return this.getDAO().queryForObject(sql, rowMapper, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	public int update(String sql, Object... args) throws DataAccessException {

		return this.getDAO().update(sql, args);
	}

	public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
		try {
			return this.getDAO().query(sql, rowMapper, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

}
