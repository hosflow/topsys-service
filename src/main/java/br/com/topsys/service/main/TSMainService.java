package br.com.topsys.service.main;

import java.lang.reflect.ParameterizedType;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.topsys.base.constant.Endpoint;
import br.com.topsys.base.exception.TSApplicationException;
import br.com.topsys.base.exception.TSSystemException;
import br.com.topsys.base.model.TSAccessControlModel;
import br.com.topsys.base.model.TSLazyModel;
import br.com.topsys.base.model.TSMainModel;
import br.com.topsys.base.util.TSType;
import br.com.topsys.base.util.TSUtil;
import br.com.topsys.service.security.TSTokenService;
import jakarta.validation.Valid;

public abstract class TSMainService<T extends TSMainModel> {

	protected abstract TSMainRepository<T> getRepository();

	@Autowired
	private TSTokenService tokenService;

	@GetMapping(value = "/{id}")
	public T get(@PathVariable(required = true) Long id) {

		return this.getRepository().get(id);
	}
	
	@GetMapping(value = Endpoint.GET_ALL)
	public List<T> getAll() {

		return this.getRepository().all();
	}

	@GetMapping(value = Endpoint.GET_HISTORY)
	public T getHistory(@PathVariable(required = true) Long id) {

		return this.getRepository().getHistory(id);
	}

	@PostMapping(value = Endpoint.FIND)
	public List<T> find(@RequestBody T model) {

		return this.getRepository().find(model);
	}

	@PostMapping(value = Endpoint.FIND_HISTORY)
	public List<T> findHistory(@RequestBody @Valid T model) {

		return this.getRepository().findHistory(model);
	}

	@PostMapping(value = Endpoint.FIND_LAZY)
	public List<T> find(@RequestBody TSLazyModel<T> lazyModel) {

		return this.getRepository().find(lazyModel.getModel(), lazyModel.getPage(), lazyModel.getSize());
	}

	@PostMapping(value = Endpoint.ROWCOUNT)
	public Integer rowCount(@RequestBody T model) {

		return this.getRepository().rowCount(model);

	}

	@PostMapping
	public T insert(@RequestBody T model) {

		this.validFieldsInsert(model);

		this.validFields(model);

		this.injectAccessControl(model);

		this.insertBusinessRule(model);

		model.setDataCadastro(OffsetDateTime.now());

		return this.getRepository().insert(model);

	}

	@PutMapping
	public T update(@RequestBody @Valid T model) {
		
		this.validField("id", model.getId());

		this.validFieldsUpdate(model);

		this.validFields(model);

		this.injectAccessControl(model);

		this.updateBusinessRule(model);

		model.setDataAtualizacao(OffsetDateTime.now());

		return this.getRepository().update(model);

	}

	@DeleteMapping(value = "/{id}")
	public T delete(@PathVariable(required = true) Long id) {
		T model; 
	
		try {
			
			@SuppressWarnings("unchecked")
			Class<T> modelClass = (Class<T>) ((ParameterizedType) getClass()
	                .getGenericSuperclass())
	                .getActualTypeArguments()[0];
			
			model = modelClass.getDeclaredConstructor().newInstance();
			
		}catch (Exception e) {
			throw new TSSystemException(e);
		}
		
		model.setId(id);
		
		this.injectAccessControl(model);

		this.deleteBusinessRule(model);

		model.setDataAtualizacao(OffsetDateTime.now());

		return this.getRepository().delete(model);

	}

	protected void validFields(Object... objects) {

		if (TSUtil.isEmpty(objects)) {

			throw new TSApplicationException("Campos obrigátorios", TSType.ERROR);
		}

	}

	protected void validFields(Map<String, Object> map) {

		StringBuilder builder = new StringBuilder();

		map.forEach((chave, valor) -> {

			if (TSUtil.isEmpty(valor)) {
				builder.append(chave).append(",");
			}

		});

		if (!builder.isEmpty()) {

			builder.insert(0, "Campos obrigatórios: [");

			builder.deleteCharAt(builder.length() - 1).append("]");

			throw new TSApplicationException(builder.toString(), TSType.ERROR);
		}

	}

	protected void validFields(T model) {
		// metodo para ser implementado se quiser validar outros campos no insert e
		// update.
	}

	protected void validFieldsInsert(T model) {
	}

	protected void validFieldsUpdate(T model) {
	}

	protected void injectAccessControl(TSMainModel model) {
		
		Map<String, Object> claims = this.tokenService.getClaims();

		if(claims != null) {
	
			TSAccessControlModel controlModel = new TSAccessControlModel();
		
			if (!TSUtil.isEmpty(claims.get("origemId"))) {
				controlModel.setOrigemId(((Integer) claims.get("origemId")).longValue());
			}
	
			if (!TSUtil.isEmpty(claims.get("usuarioFuncaoId"))) {
				controlModel.setUsuarioFuncaoId(((Integer) claims.get("usuarioFuncaoId")).longValue());
			}
	
			if (!TSUtil.isEmpty(claims.get("id"))) {
				controlModel.setUsuarioId(((Integer) claims.get("id")).longValue());
			}
			
			model.setControleAcesso(controlModel);

		}
		
	}

	protected void validAutoComplete(String field, int min) {
		if (TSUtil.isEmpty(field) || field.length() < min) {
			throw new TSApplicationException("AutoComplete: é necessário ao menos %d caracteres".formatted(min),
					TSType.ERROR);
		}
	}

	protected void validField(String nome, Object id) {
		Map<String, Object> map = new HashMap<>();
		map.put(nome, id);

		this.validFields(map);

	}

	protected void updateBusinessRule(T model) {
	}

	protected void insertBusinessRule(T model) {
	}

	protected void deleteBusinessRule(T model) {
	}

}
