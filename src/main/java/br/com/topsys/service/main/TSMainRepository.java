package br.com.topsys.service.main;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.topsys.base.exception.TSSystemException;
import br.com.topsys.base.model.TSMainModel;
import br.com.topsys.service.jdbc.TSJdbcTemplate;
import br.com.topsys.service.jdbc.TSModelRowMapper;


public abstract class TSMainRepository<T extends TSMainModel>  {

	private static final String NOT_IMPLEMENTED = "Método não implementando no Repository";
	
	@Autowired
	protected TSJdbcTemplate jdbcTemplate;
	
	
	public List<T> all() {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

	public List<T> find(T model) {
		return this.find(model, 0, 0);
	}
	
	public List<T> findHistory(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(T model, int page, int size) {
		var query = new StringBuilder();
		var params = new ArrayList<>();
		var columns = new ArrayList<String>();
		
		Class<T> type = (Class<T>) model.getClass();
	
		this.prepareFind(model, query, params, columns);
		
		if (size > 0) {
			query.append(" LIMIT ? OFFSET ? ");
			params.add(size);
			params.add(page * size);
		}
			
		return this.jdbcTemplate.query(query.toString(), new TSModelRowMapper<T>(type, columns.toArray(new String[]{})), params.toArray());
	}
	
	public Integer rowCount(T model) {
		var query = new StringBuilder();
		var params = new ArrayList<Object>();
		
		query.append("select count(1) from (");
		
		this.prepareFind(model, query, params, null);
		
		query.append(") sub");
		
		return this.jdbcTemplate.getRowCount(query.toString(), params.toArray());
	}
	
	protected void prepareFind(T model, StringBuilder query, List<Object> params, List<String> columns) {
    	throw new TSSystemException(NOT_IMPLEMENTED);
    }
    

    public T get(Long id) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}
	
    public T getHistory(Long id) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

    public T update(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

    public T insert(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

    public T delete(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}
}
