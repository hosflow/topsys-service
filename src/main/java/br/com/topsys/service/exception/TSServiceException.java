package br.com.topsys.service.exception;

import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.topsys.base.exception.TSApplicationException;
import br.com.topsys.base.exception.TSSystemException;
import br.com.topsys.base.model.TSResponseExceptionModel;
import br.com.topsys.base.util.TSType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice 
@Component
public class TSServiceException {	

	private static final String ERRO_INTERNO = "Ocorreu um erro interno, entre em contato com a TI!";

	@ExceptionHandler({ NullPointerException.class, TSSystemException.class, DataAccessException.class })
	public ResponseEntity<Object> handleException(RuntimeException ex) {
		
		log.error(ex.getMessage());
		
		return new ResponseEntity<>(
				new TSResponseExceptionModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), ERRO_INTERNO),
				HttpStatus.INTERNAL_SERVER_ERROR);
	} 
 
	@ExceptionHandler(TSApplicationException.class)
	public ResponseEntity<Object> handleException(TSApplicationException ex) {
		
		HttpStatus type = ex.getTSType().equals(TSType.BUSINESS) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		return new ResponseEntity<>(new TSResponseExceptionModel(type.value(), new Date(), ex.getMessage()),
				type);
 
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<Object> handleException(DuplicateKeyException ex) {
		
		return new ResponseEntity<>(
				new TSResponseExceptionModel(HttpStatus.BAD_REQUEST.value(), new Date(), "Já existe esse registro!"),
				HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleException(DataIntegrityViolationException ex) {
		
		return new ResponseEntity<>(
				new TSResponseExceptionModel(HttpStatus.BAD_REQUEST.value(), new Date(), "Existem registros dependentes!"),
				HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<Object> handleException(EmptyResultDataAccessException ex) {
		
		return new ResponseEntity<>(
				new TSResponseExceptionModel(HttpStatus.OK.value(), new Date(), "Não retornou nenhum registro!"),
				HttpStatus.OK); 

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {
		
		return new ResponseEntity<>(
				new TSResponseExceptionModel(HttpStatus.BAD_REQUEST.value(), new Date(), "Campos obrigatórios!"),
				HttpStatus.BAD_REQUEST);

	}


}
