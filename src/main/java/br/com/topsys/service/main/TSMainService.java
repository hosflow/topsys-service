package br.com.topsys.service.main;

import java.io.Serializable;
import java.util.List;


public abstract class TSMainService<T extends Serializable> {
	
	
	public abstract TSMainDAO<T> getDAO();
	
	
    public List<T> pesquisar(T model){
    	return this.getDAO().pesquisar(model);
    }
	
	public T obter(T model) {
		return this.getDAO().obter(model);
	}
	
	public T alterar(T model) {
		return this.getDAO().alterar(model);
	}
		
	public T inserir(T model) {
		return this.getDAO().inserir(model);
	}
		
	public T excluir(T model) {
		return this.getDAO().excluir(model);
	}
	

}
