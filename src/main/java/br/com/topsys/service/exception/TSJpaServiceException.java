package br.com.topsys.service.exception;

import java.util.Date;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
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
	

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleException400(HttpMessageNotReadableException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> handleExceptionBadCredentials() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
	}
	

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Object> handleExceptionAuthentication() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
	}
	

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleExceptionAcessoNegado() {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado");
	}
	

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException500(Exception ex) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + ex.getLocalizedMessage());
	}
	
	
	

	private record ErroValidacao(String field, String message) {
		public ErroValidacao(FieldError fieldError) {
			this(fieldError.getField(), fieldError.getDefaultMessage());
		}
	}

}
