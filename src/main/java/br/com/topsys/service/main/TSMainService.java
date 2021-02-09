package br.com.topsys.service.main;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


public abstract class TSMainService<T extends Serializable> {

	public abstract TSMainRepository<T> getRepository();

	@PostMapping(value = "/obter")
	public T obter(@RequestBody T model) {

		return this.getRepository().obter(model);
	}
 
	@PostMapping(value = "/pesquisar")
	public List<T> pesquisar(@RequestBody T model) {

		return this.getRepository().pesquisar(model);
	}

	@PostMapping(value = "/inserir")
	public T inserir(@RequestBody T model) {

		return this.getRepository().inserir(model);

	}

	@PostMapping(value = "/alterar")
	public T alterar(@RequestBody T model) {

		return this.getRepository().alterar(model);

	}

	@PostMapping(value = "/excluir")
	public T excluir(@RequestBody T model) {

		return this.getRepository().excluir(model);

	}

}
