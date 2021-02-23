package br.com.topsys.service.main;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.topsys.base.model.TSLazyModel;

public abstract class TSMainService<T extends Serializable> {

	protected abstract TSMainRepository<T> getRepository();

	@PostMapping(value = "/get")
	public T get(@RequestBody T model) {

		return this.getRepository().get(model);
	}

	@PostMapping(value = "/find")
	public List<T> find(@RequestBody T model) {

		return this.getRepository().find(model);
	}

	@PostMapping(value = "/find-lazy")
	public List<T> find(@RequestBody TSLazyModel<T> lazyModel) {

		return this.getRepository().find(lazyModel.getModel(), lazyModel.getOffset(), lazyModel.getSize());
	}

	@PostMapping(value = "/rowcount")
	public Integer rowCount(@RequestBody T model) {
		return this.getRepository().rowCount(model);
	}

	@PostMapping(value = "/insert")
	public T insert(@RequestBody @Valid T model) {

		return this.getRepository().insert(model);

	}

	@PostMapping(value = "/update")
	public T update(@RequestBody @Valid T model) {

		return this.getRepository().update(model);

	}

	@PostMapping(value = "/delete")
	public T delete(@RequestBody T model) {

		return this.getRepository().delete(model);

	}

}
