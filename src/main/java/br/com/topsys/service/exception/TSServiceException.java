package br.com.topsys.service.exception;

import java.util.Date;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

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
import br.com.topsys.base.model.TSResponseExceptionModel;
import br.com.topsys.base.util.TSType;

@RestControllerAdvice
@Component
public class TSServiceException {

	private static final String ERRO_INTERNO = "Ocorreu um erro interno, entre em contato com a TI!";

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception ex) {

		ex.printStackTrace();

		return new ResponseEntity<>(TSResponseExceptionModel.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(ERRO_INTERNO).trace(ex.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(TSApplicationException.class)
	public ResponseEntity<Object> handleException(TSApplicationException ex) {

		HttpStatus type = ex.getTSType().equals(TSType.BUSINESS) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(TSResponseExceptionModel.builder().status(type.value()).timestamp(new Date())
				.message(ex.getMessage()).build(), type);

	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<Object> handleException(DuplicateKeyException ex) {

		return new ResponseEntity<>(TSResponseExceptionModel.builder().status(HttpStatus.BAD_REQUEST.value())
				.timestamp(new Date()).message("Já existe esse registro!").build(), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleException(DataIntegrityViolationException ex) {

		return new ResponseEntity<>(TSResponseExceptionModel.builder().status(HttpStatus.BAD_REQUEST.value())
				.timestamp(new Date()).message("Já existe esse registro!").build(), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<Object> handleException(EmptyResultDataAccessException ex) {

		return new ResponseEntity<>(TSResponseExceptionModel.builder().status(HttpStatus.NOT_FOUND.value())
				.timestamp(new Date()).message("Nenhum registro encontrado!").build(), HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {

		return new ResponseEntity<>(TSResponseExceptionModel.builder().status(HttpStatus.BAD_REQUEST.value())
				.timestamp(new Date())
				.message("Campos obrigatórios: ["
						+ ex.getFieldErrors().stream().map(e -> e.getField()).collect(Collectors.joining(", ")) + "]")
				.build(), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleException404() {

		return ResponseEntity.notFound().build();

	}

	@ExceptionHandler(EntityExistsException.class)
	public ResponseEntity<Object> handleExceptionExists() {

		return ResponseEntity.badRequest().body(TSResponseExceptionModel.builder().message("Já existe esse registro!"));

	}

	/*
	 * @ExceptionHandler(DataIntegrityViolationException.class) public
	 * ResponseEntity<?> handleDataIntegrityViolationException() {
	 * 
	 * return ResponseEntity.badRequest().body(TSResponseExceptionModel.builder().
	 * message("Já existe esse registro!"));
	 * 
	 * }
	 * 
	 */


}
