package br.com.topsys.base.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class TSLazyModel<T extends TSMainModel> implements Serializable {

	private T model;
	
	private int page;
	private int size;

	public TSLazyModel(T model, int page, int size) {
		this.model = model;
		this.page = page;
		this.size = size;
	}

}
