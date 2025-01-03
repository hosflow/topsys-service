package br.com.topsys.base.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TSMessageModel implements Serializable {

	private Integer codigo;
	private TSTypeAlertEnum tipo;
	private String mensagem;
		
	public TSMessageModel(TSTypeAlertEnum tipo, String mensagem) {
		this.tipo = tipo;
		this.mensagem = mensagem;
	}


}
