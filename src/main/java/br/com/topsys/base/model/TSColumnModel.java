package br.com.topsys.base.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
public class TSColumnModel implements Serializable {
	
	private List<TSAttributeModel> attributes = new ArrayList<>(); 
	
	
	public void addAttribute(String value) {
		this.attributes.add(new TSAttributeModel(value)); 
	}
	
	public List<TSAttributeModel> getAttributes(){
		return this.attributes; 
	}
}
