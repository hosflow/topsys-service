package br.com.topsys.base.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TSResponseExceptionModel implements Serializable {
	private Date timestamp;
	
	private String error;
	private String message;  
	private String path; 
	private String trace;
	
	private int status;
	
}