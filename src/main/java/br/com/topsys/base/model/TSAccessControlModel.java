package br.com.topsys.base.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class TSAccessControlModel implements Serializable {
	
	private Long usuarioId;
	private Long usuarioFuncaoId;
	private Long origemId;
	private boolean flagAdministrador;


}
