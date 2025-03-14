package br.com.topsys.base.model;

public interface TSSecurityModel {
	
	
	default void setId(Long id) {}
	default Long getId() {return null;}
	
	default void setUsuarioFuncaoId(Long id) {};
	default Long getUsuarioFuncaoId() {return null;}
	
	default void setOrigemId(Long id) {};
	default Long getOrigemId(){return null;}
	
	default void setLogin(String login) {};
	default String getLogin(){return null;}
	
	default void setToken(String token) {};
	default String getToken(){return null;}
	
	default void setRefreshToken(String token) {};
	default String getRefreshToken(){return null;}
	
	default void setSenha(String senha) {};
	default String getSenha(){return null;}
	
	default void setFlagAdministrador(boolean flag) {};
	default boolean isFlagAdministrador(){return false;}
	
	
	

}
