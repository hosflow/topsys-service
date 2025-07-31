package br.com.topsys.base.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
	
	protected OffsetDateTime dataOperacao;
	
	protected Boolean flagAtivo; 	
	
	protected List<TSMessageModel> messages;
	
	protected TSUsuarioFuncaoModel usuarioFuncaoCadastroModel;

	protected TSUsuarioFuncaoModel usuarioFuncaoAtualizacaoModel;
	
	public String getDataCadastroFormatada() {
		return this.dataCadastro != null ? this.dataCadastro.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault())) : "";
	}
	
	public String getDataAtualizacaoFormatada() {
		return this.dataAtualizacao != null ? this.dataAtualizacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault())) : "";
	}

	
	

}
