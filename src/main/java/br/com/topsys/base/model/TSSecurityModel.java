package br.com.topsys.base.model;

public interface TSSecurityModel {
	
	public Long getId();
	
	public Long getUsuarioFuncaoId();
	
	public Long getOrigemId();
	
	public void setLogin(String login);
	public String getLogin();
	
	public void setToken(String token);
	public String getToken();
	
	public void setRefreshToken(String token);
	public String getRefreshToken();
	
	public void setSenha(String senha);
	public String getSenha();
	
	
	

}
