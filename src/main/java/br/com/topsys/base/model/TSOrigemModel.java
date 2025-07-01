package br.com.topsys.base.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TSOrigemModel extends TSMainModel {

	private String descricao;
	
	public TSOrigemModel(Long id) {
		this.id = id;
	}

	public TSOrigemModel(Boolean flagAtivo) {
		this.flagAtivo = flagAtivo;
	}

}
