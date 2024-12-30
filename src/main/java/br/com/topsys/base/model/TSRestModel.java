package br.com.topsys.base.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@Builder
public class TSRestModel implements Serializable {
	
	private String baseUrl;
	private String url; 
	private String token;
	
	private Object model;
	
	
	
}
