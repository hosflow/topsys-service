package br.com.topsys.service.main;

import java.util.List;

import br.com.topsys.base.exception.TSSystemException;
import br.com.topsys.base.model.TSMainModel;


public interface TSMainRepository<T extends TSMainModel>  {

	static final String NOT_IMPLEMENTED = "Método não implementando no Repository";
	
	default List<T> all() {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

	default List<T> find(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}
	
	default List<T> findHistory(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

	default List<T> find(T model, int offset, int size) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}
	
	default Integer rowCount(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

	default T get(Long id) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}
	
	default T getHistory(Long id) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

	default T update(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

	default T insert(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}

	default T delete(T model) {
		throw new TSSystemException(NOT_IMPLEMENTED);
	}
}
