package br.com.topsys.base.model;

public enum TSTypeAlertEnum {
	
	ALERT(1), INFO(2), BUSINESS_ROLE(3);
	
	private Integer code;
	
	TSTypeAlertEnum(Integer code){
		this.code = code;
	}
	
	public Integer getCode(Integer code) {
		return this.code;
	}

}
