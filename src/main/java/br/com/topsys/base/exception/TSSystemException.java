package br.com.topsys.base.exception;

public class TSSystemException extends RuntimeException {

	public TSSystemException(Exception e) {
		super(e);
	}

	public TSSystemException(String mensagem) {
		super(mensagem);
	}

	public TSSystemException(String mensagem, Exception e) {
		super(mensagem, e);
	}

}
