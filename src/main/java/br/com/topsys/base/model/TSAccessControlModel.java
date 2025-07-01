package br.com.topsys.base.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class TSAccessControlModel implements Serializable {
	
	private Long usuarioId;
	private String usuarioNome;
	
	private Long funcaoId;
	private Long usuarioFuncaoId;
	private String funcaoDescricao;
	
	private Long origemId;
	private String origemDescricao;
	
	private Long setorId;
	private String setorDescricao;
	
	private boolean flagAdministrador;
	
	
	
	
	

}
