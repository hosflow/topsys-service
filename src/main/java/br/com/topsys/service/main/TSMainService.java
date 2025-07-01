package br.com.topsys.service.main;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.topsys.base.exception.TSApplicationException;
import br.com.topsys.base.model.TSMainModel;
import br.com.topsys.base.util.TSType;
import br.com.topsys.base.util.TSUtil;

public abstract class TSMainService<T extends TSMainModel> {
	
	protected abstract TSMainRepository<T> getRepository();

	public T get(Long id) {

		return this.getRepository().get(id);
	}

	public List<T> getAll() {

		return this.getRepository().all();
	}

	public T getHistory(Long id) {

		return this.getRepository().getHistory(id);
	}

	public List<T> find(T model) {

		return this.getRepository().find(model);
	}

	public List<T> findHistory(T model) {

		return this.getRepository().findHistory(model);
	}

	public List<T> find(T model, int page, int size) {

		return this.getRepository().find(model, page, size);
	}

	public Integer rowCount(T model) {

		return this.getRepository().rowCount(model);

	}

	public T insert(T model) {

		this.validFieldsInsert(model);

		this.validFields(model);

		this.insertBusinessRule(model);

		model.setDataCadastro(OffsetDateTime.now());

		return this.getRepository().insert(model);

	}

	public T update(T model) {

		this.validFieldsUpdate(model);

		this.validFields(model);

		this.updateBusinessRule(model);

		model.setDataAtualizacao(OffsetDateTime.now());

		return this.getRepository().update(model);

	}

	public T delete(T model) {

		this.deleteBusinessRule(model);

		model.setDataAtualizacao(OffsetDateTime.now());

		return this.getRepository().delete(model);

	}

	protected void validFields(Object... objects) {

		if (TSUtil.isEmpty(objects)) {

			throw new TSApplicationException("Campos obrigatórios", TSType.ERROR);
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
	
	/**
	 * metodo para ser implementado se quiser validar outros campos no insert e
	 * update.
	 * 
	 * @param model
	 */
	protected void validFields(T model) {

	}

	protected void validFieldsInsert(T model) {
	}

	protected void validFieldsUpdate(T model) {
	}


}
