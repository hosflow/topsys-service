package br.com.topsys.service.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		this.validAccessControl(model);

		return this.getRepository().get(model);
	}

	@PostMapping(value = "/get-history")
	public T getHistory(@RequestBody T model) {

		this.validAccessControl(model);

		return this.getRepository().getHistory(model);
	}

	@PostMapping(value = "/find")
	public List<T> find(@RequestBody T model) {

		this.validAccessControl(model);

		return this.getRepository().find(model);
	}

	@PostMapping(value = "/find-history")
	public List<T> findHistory(@RequestBody T model) {

		this.validAccessControl(model);

		return this.getRepository().findHistory(model);
	}

	@PostMapping(value = "/find-lazy")
	public List<T> find(@RequestBody TSLazyModel<T> lazyModel) {

		this.validAccessControl(lazyModel);

		return this.getRepository().find(lazyModel.getModel(), lazyModel.getOffset(), lazyModel.getSize());
	}

	@PostMapping(value = "/rowcount")
	public Integer rowCount(@RequestBody T model) {
		
		this.validAccessControl(model);

		return this.getRepository().rowCount(model);

	}

	@PostMapping(value = "/insert")
	public T insert(@RequestBody @Valid T model) {

		this.validAccessControl(model);

		this.validFields(model);

		return this.getRepository().insert(model);

	}

	@PostMapping(value = "/update")
	public T update(@RequestBody @Valid T model) {

		this.validAccessControl(model);

		this.validFields(model);

		return this.getRepository().update(model);

	}

	@PostMapping(value = "/delete")
	public T delete(@RequestBody T model) {

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
		Map<String, Object> map = new HashMap<>();
		map.put("controleAcesso.usuarioFuncaoId",
				(model.getControleAcesso() == null ? null : model.getControleAcesso().getUsuarioFuncaoId()));
		map.put("controleAcesso.origemId",
				(model.getControleAcesso() == null ? null : model.getControleAcesso().getOrigemId()));
		this.validFields(map);
	}

}
