package br.com.topsys.base.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public abstract class TSMainModel implements Serializable {

	@EqualsAndHashCode.Include
	protected Long id;

	@EqualsAndHashCode.Include 
	protected Long idHistorico;

	protected TSAccessControlModel controleAcesso;
	
	protected OffsetDateTime dataCadastro; 
	
	protected OffsetDateTime dataAtualizacao;
	
	protected Boolean flagAtivo; 	
	
	protected TSUserIf usuarioCadastroModel;	
	
	protected TSUserIf usuarioAtualizacaoModel;
	
	protected List<TSMessageModel> messages;
	
	

}
