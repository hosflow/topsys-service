package br.com.topsys.service.main;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.topsys.base.exception.TSSystemException;
import br.com.topsys.service.jdbc.TSJdbcDAO;

@Repository
public abstract class TSMainDAO<T extends Serializable> extends TSJdbcDAO {

	private static final String NAO_IMPLEMENTANDO = "Método não implementando no DAO";

	public List<T> pesquisar(T model) {
		throw new TSSystemException(NAO_IMPLEMENTANDO);
	}

	public T obter(T model) {
		throw new TSSystemException(NAO_IMPLEMENTANDO);
	}

	public T alterar(T model) {
		throw new TSSystemException(NAO_IMPLEMENTANDO);
	}

	public T inserir(T model) {
		throw new TSSystemException(NAO_IMPLEMENTANDO);
	}

	public T excluir(T model) {
		throw new TSSystemException(NAO_IMPLEMENTANDO);
	}
}
