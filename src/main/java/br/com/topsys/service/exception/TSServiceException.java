package br.com.topsys.service.exception;

import java.util.Date;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.TokenExpiredException;

import br.com.topsys.base.exception.TSApplicationException;
import br.com.topsys.base.exception.TSSystemException;
import br.com.topsys.base.model.TSResponseExceptionModel;
import br.com.topsys.base.util.TSType;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
@Component
public class TSServiceException {

	private static final String ERRO_INTERNO = "Ocorreu um erro interno, entre em contato com a TI!";

	
	
	@ExceptionHandler({TSSystemException.class, RuntimeException.class, Exception.class})
	public ResponseEntity<Object> handleException(Exception ex) {
       
		ex.printStackTrace();
 
		return ResponseEntity.internalServerError().body(TSResponseExceptionModel.builder()
				.timestamp(new Date())
				.message(ERRO_INTERNO)
				.trace(ex.getMessage())
				.build());

	}
	
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<Object> handleException(TokenExpiredException ex) {

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED) 
				.body(TSResponseExceptionModel.builder()
				.timestamp(new Date())
				.message("Token expirado ou inválido!")
				.trace(ex.getMessage())
				.build());

	}
	
	

	@ExceptionHandler(TSApplicationException.class)
	public ResponseEntity<Object> handleException(TSApplicationException ex) {

		HttpStatus type = ex.getTSType().equals(TSType.BUSINESS) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(TSResponseExceptionModel.builder().status(type.value()).timestamp(new Date())
				.message(ex.getMessage()).build(), type);

	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<Object> handleException(DuplicateKeyException ex) {

		return ResponseEntity.badRequest().body(TSResponseExceptionModel.builder()
				.timestamp(new Date())
				.message("Já existe esse registro!")
				.build());

	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleException(DataIntegrityViolationException ex) {

		return ResponseEntity.badRequest().body(TSResponseExceptionModel.builder()
				.timestamp(new Date())
				.message(ex.getMessage())
				.build());

	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<Object> handleException(EmptyResultDataAccessException ex) {

		return new ResponseEntity<>(TSResponseExceptionModel.builder()
				.status(HttpStatus.NOT_FOUND.value())
				.timestamp(new Date())
				.message("Nenhum registro encontrado!").build(), HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {
		StringBuilder builder = new StringBuilder();
		
		ex.getBindingResult().getFieldErrors().forEach(error -> 
			builder.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("\n")
		);
      

		return ResponseEntity.badRequest().body(TSResponseExceptionModel.builder()
				.timestamp(new Date())
				.message(builder.toString()));
				
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleException404() {

		return ResponseEntity.notFound().build();

	}

	@ExceptionHandler(EntityExistsException.class)
	public ResponseEntity<Object> handleExceptionExists() {

		return ResponseEntity.badRequest().body(TSResponseExceptionModel.builder()
				.timestamp(new Date())
				.message("Já existe esse registro!")
				.build());

	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> handleException(BadCredentialsException ex) {

		return ResponseEntity.badRequest().body(TSResponseExceptionModel.builder()
				.timestamp(new Date())
				.message("Usuário ou senha não confere!")
				.build());

	}
	

}
 