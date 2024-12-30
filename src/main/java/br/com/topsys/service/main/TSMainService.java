package br.com.topsys.service.main;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.topsys.base.constant.Endpoint;
import br.com.topsys.base.exception.TSApplicationException;
import br.com.topsys.base.model.TSLazyModel;
import br.com.topsys.base.model.TSMainModel;
import br.com.topsys.base.util.TSType;
import br.com.topsys.base.util.TSUtil;

public abstract class TSMainService<T extends TSMainModel> {

	protected abstract TSMainRepository<T> getRepository();
	
	@Value("${topsys.service.isservice}")
	private boolean isService = false;

	@GetMapping
	public T get(@RequestBody T model) {

		this.validFieldId("id",model.getId());

		this.validAccessControl(model);
 
		return this.getRepository().get(model);
	}

	@GetMapping(value = Endpoint.GET_HISTORY)
	public T getHistory(@RequestBody T model) {
         
		this.validFieldId("idHistorico",model.getIdHistorico());
		
		this.validAccessControl(model);

		return this.getRepository().getHistory(model);
	}

	@PostMapping(value = Endpoint.FIND)
	public List<T> find(@RequestBody T model) {

		this.validAccessControl(model);

		return this.getRepository().find(model);
	}

	@PostMapping(value = Endpoint.FIND_HISTORY)
	public List<T> findHistory(@RequestBody T model) {

		this.validFieldId("id",model.getId());

		this.validAccessControl(model);

		return this.getRepository().findHistory(model);
	}

	@PostMapping(value = Endpoint.FIND_LAZY)
	public List<T> find(@RequestBody TSLazyModel<T> lazyModel) {

		this.validAccessControl(lazyModel.getModel());

		return this.getRepository().find(lazyModel.getModel(), lazyModel.getOffset(), lazyModel.getSize());
	}

	@GetMapping(value = Endpoint.ROWCOUNT)
	public Integer rowCount(@RequestBody T model) {

		this.validAccessControl(model);

		return this.getRepository().rowCount(model);

	}

	@PostMapping
	public T insert(@RequestBody @Valid T model) {

		this.validAccessControl(model);

		this.validFields(model);
		
		model.setDataCadastro(OffsetDateTime.now());

		return this.getRepository().insert(model);

	}

	@PutMapping
	public T update(@RequestBody @Valid T model) {

		this.validFieldId("id",model.getId());

		this.validAccessControl(model);

		this.validFields(model);
		
		model.setDataAtualizacao(OffsetDateTime.now());

		return this.getRepository().update(model);

	}

	@DeleteMapping
	public T delete(@RequestBody T model) {

		this.validFieldId("id",model.getId());

		this.validAccessControl(model);

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

	protected void validAccessControl(TSMainModel model) {
		
		if(isService) {
			Map<String, Object> map = new HashMap<>();
			map.put("controleAcesso.usuarioFuncaoId",
					(model.getControleAcesso() == null ? null : model.getControleAcesso().getUsuarioFuncaoId()));
			map.put("controleAcesso.origemId",
					(model.getControleAcesso() == null ? null : model.getControleAcesso().getOrigemId()));
			this.validFields(map);
		}
		
		
	}

	protected void validAutoComplete(String field, int min) {
		if (TSUtil.isEmpty(field) || field.length() < min) {
			throw new TSApplicationException("AutoComplete: é necessário ao menos %d caracteres".formatted(min),
					TSType.ERROR);
		}
	}

	private void validFieldId(String nome, Long id) {
		Map<String, Object> map = new HashMap<>();
		map.put(nome, id);

		this.validFields(map);

	}

}
