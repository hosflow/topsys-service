package br.com.topsys.base.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public abstract class TSMainModel implements Serializable {

	@EqualsAndHashCode.Include
	@NotNull
	protected Long id;

	@EqualsAndHashCode.Include 
	protected Long idHistorico;

	protected TSAccessControlModel controleAcesso;
	
	protected OffsetDateTime dataCadastro; 
	
	protected OffsetDateTime dataAtualizacao;
	
	protected Boolean flagAtivo = false; 	
	
	protected TSUserModel usuarioCadastroModel;	
	
	protected TSUserModel usuarioAtualizacaoModel;
	
	protected List<TSMessageModel> messages;
	
	

}
