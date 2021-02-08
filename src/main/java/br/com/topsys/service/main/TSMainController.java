package br.com.topsys.service.main;

import java.io.Serializable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.topsys.base.model.TSRetornoModel;

public abstract class TSMainController<T extends Serializable> {

	public abstract TSMainService<T> getService();

	@PostMapping(value = "/obter")
	public TSRetornoModel<T> obter(@RequestBody T model) {

		return new TSRetornoModel<T>(this.getService().obter(model));
	}

	@PostMapping(value = "/pesquisar")
	public TSRetornoModel<T> pesquisar(@RequestBody T model) {

		return new TSRetornoModel<T>(this.getService().pesquisar(model));
	}

	@PostMapping(value = "/inserir")
	public TSRetornoModel<T> inserir(@RequestBody T model) {

		return new TSRetornoModel<T>(this.getService().inserir(model));

	}

	@PostMapping(value = "/alterar")
	public TSRetornoModel<T> alterar(@RequestBody T model) {

		return new TSRetornoModel<T>(this.getService().alterar(model));

	}

	@PostMapping(value = "/excluir")
	public TSRetornoModel<T> excluir(@RequestBody T model) {

		return new TSRetornoModel<T>(this.getService().excluir(model));

	}

}
