package br.com.topsys.base.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@ToString(of = "id")
public abstract class TSMainModel implements Serializable {

	@EqualsAndHashCode.Include
	protected Long id;

	@EqualsAndHashCode.Include 
	protected Long idHistorico;

	protected TSAccessControlModel accessControlModel;
	
	protected OffsetDateTime dataCadastro; 
	
	protected OffsetDateTime dataAtualizacao;
	
	protected Boolean flagAtivo; 	
	
	protected List<TSMessageModel> messages;
	
	protected TSUsuarioFuncaoModel usuarioFuncaoCadastroModel;

	protected TSUsuarioFuncaoModel usuarioFuncaoAtualizacaoModel;
	
	

}
