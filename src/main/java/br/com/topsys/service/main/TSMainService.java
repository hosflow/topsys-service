package br.com.topsys.service.main;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.topsys.base.exception.TSApplicationException;
import br.com.topsys.base.model.TSLazyModel;
import br.com.topsys.base.model.TSMainModel;
import br.com.topsys.base.util.TSType;
import br.com.topsys.base.util.TSUtil;

public abstract class TSMainService<T extends TSMainModel> {

	protected abstract TSMainRepository<T> getRepository();

	@PostMapping(value = "/get")
	public T get(@RequestBody T model) {

		return this.getRepository().get(model);
	}
	
	@PostMapping(value = "/get-history")
	public T getHistory(@RequestBody T model) {

		return this.getRepository().getHistory(model);
	}

	@PostMapping(value = "/find")
	public List<T> find(@RequestBody T model) {

		return this.getRepository().find(model);
	}
	
	@PostMapping(value = "/find-history")
	public List<T> findHistory(@RequestBody T model) {

		return this.getRepository().findHistory(model);
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
		
		this.validFields();
		
		return this.getRepository().insert(model);

	} 

	@PostMapping(value = "/update")
	public T update(@RequestBody @Valid T model) {
		
		this.validFields();
		
		return this.getRepository().update(model);

	}

	@PostMapping(value = "/delete")
	public T delete(@RequestBody T model) {

		return this.getRepository().delete(model);

	}
	
	protected void validFields(Object...objects) {
		
		if(TSUtil.isEmpty(objects)) {
			
			throw new TSApplicationException("Campos obrigátorios",TSType.ERROR);
		}
		
	}
	
	protected void validFields() {
		// metodo para ser implementado se quiser validar outros campos no insert e update.
	}
	

}
