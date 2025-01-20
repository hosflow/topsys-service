package br.com.topsys.base.model;

public interface TSSecurityModel {
	
	
	public void setId(Long id);
	public Long getId();
	
	public void setUsuarioFuncaoId(Long id);
	public Long getUsuarioFuncaoId();
	
	public void setOrigemId(Long id);
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
