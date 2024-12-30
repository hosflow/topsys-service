package br.com.topsys.base.model;

import java.io.Serializable;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class TSControleAcessoModel implements Serializable {
	
	private Long usuarioId;
	private Long moduloId;
	private Long usuarioFuncaoId;
	private Long funcaoId;
	private Long origemId;
	private Long menuAtualId;
	private Long grupoId;
	 
	private String nomeUsuario;
	private String loginUsuario;
	private String nomeFuncao;
	private String nomeModulo; 
	private String nomeOrigem;
	private String nomeGrupo;
	private String slug;
	private String abreviacao;
	private String imagemFundo;
	
	private Boolean flagAdmin;
	
	

}
