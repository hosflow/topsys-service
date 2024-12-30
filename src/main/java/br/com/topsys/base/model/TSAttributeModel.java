package br.com.topsys.base.model;

import java.io.Serializable;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class TSAttributeModel implements Serializable {
	 
	private String value;  
	
	public TSAttributeModel() {
		
	}
	
	public TSAttributeModel(String value) {
		this.value = value;
	}
	
	
	
}
