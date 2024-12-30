package br.com.topsys.base.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;


@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class TSLazyModel<T extends TSMainModel> implements Serializable {

	private T model;
	
	private int offset;
	private int size;

	public TSLazyModel(T model, int offset, int size) {
		this.model = model;
		this.offset = offset;
		this.size = size;
	}

}
