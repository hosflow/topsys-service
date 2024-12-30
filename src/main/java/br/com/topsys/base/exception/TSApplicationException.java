package br.com.topsys.base.exception;

import br.com.topsys.base.util.TSType;

@SuppressWarnings("serial")
public class TSApplicationException extends RuntimeException {

	private TSType tsType = TSType.BUSINESS;

	public TSApplicationException(Exception e) {
		super(e);
	}

	public TSApplicationException(String mensagem, TSType type) {
		super(mensagem);
		this.tsType = type;
	}

	public TSApplicationException(String mensagem) {
		super(mensagem);
	}

	public TSApplicationException(String mensagem, Exception e) {
		super(mensagem, e);
	}

	public TSType getTSType() {
		return this.tsType;
	}

}
