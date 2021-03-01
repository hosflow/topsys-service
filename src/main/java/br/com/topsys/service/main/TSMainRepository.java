package br.com.topsys.service.main;

import java.util.List;

import br.com.topsys.base.exception.TSSystemException;
import br.com.topsys.base.model.TSMainModel;
import br.com.topsys.service.jdbc.TSJdbcRepository;


public abstract class TSMainRepository<T extends TSMainModel> extends TSJdbcRepository {

	private static final String NOT_IMPLEMENTED = "Método não implementando no Repository";

	public List<T> find(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}
	
	public List<T> findHistory(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

	public List<T> find(T model, int offset, int size) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}
	
	public List<T> findHistory(T model, int offset, int size) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

	public Integer rowCount(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

	public T get(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}
	
	public T getHistory(T model) {
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
