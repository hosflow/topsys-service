package br.com.topsys.service.exception;

import java.util.Date;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.com.topsys.base.model.TSResponseExceptionModel;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
@Component
public class TSJpaServiceException {

	

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleException(DataIntegrityViolationException ex) {

		return new ResponseEntity<>(TSResponseExceptionModel.builder().status(HttpStatus.BAD_REQUEST.value())
				.timestamp(new Date()).message("Já existe esse registro!").build(), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleException404() {

		return ResponseEntity.notFound().build();

	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleException400(MethodArgumentNotValidException ex) {
		
		var erros = ex.getFieldErrors();
		
		return ResponseEntity.badRequest().body(erros.stream().map(ErroValidacao::new).toList());

	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleException400() {
		
		return ResponseEntity.badRequest().build();

	}
	
	

	
	private record ErroValidacao(String field, String message) { 
		public ErroValidacao(FieldError fieldError){
			this(fieldError.getField(),fieldError.getDefaultMessage());
		}
	}



}
