package br.com.topsys.base.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TSUserModel extends TSMainModel {
	
	public String nome;
	public String login;
	public String senha;
	public String cpf;
	public String email;
	public Boolean flagAdministrador = false;
	
	

}
