package br.com.topsys.service.exception;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import br.com.topsys.base.exception.TSApplicationException;
import br.com.topsys.base.exception.TSSystemException;

@Aspect
@Component
public class TSAspectServiceException {

	@Around("execution(public * br.com..*service..*(..))")
	public Object handleExceptions(ProceedingJoinPoint joinPoint) throws Throwable {

		try {

			return joinPoint.proceed();
			
		} catch (TSApplicationException ex) {
			
			throw ex;

		}catch (TSSystemException ex) {
				
			throw ex;

		} catch (Exception ex) {

			handle(ex);

		}
		return null;

	}

	public void handle(Exception ex) {

		if (ex instanceof DuplicateKeyException) {

			throw new TSApplicationException("já existe esse registro");

		} else if (ex instanceof EmptyResultDataAccessException) {

			throw new TSApplicationException("Nenhum registro encontrado!");

		} else if (ex instanceof DataIntegrityViolationException) {

			throw new TSApplicationException("Existem registros dependentes!");

		} else if (ex instanceof MethodArgumentNotValidException) {
			StringBuilder builder = new StringBuilder();

			MethodArgumentNotValidException argumentNotValidException = (MethodArgumentNotValidException) ex;

			argumentNotValidException.getBindingResult().getFieldErrors().forEach(error -> builder
					.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("\n"));

			throw new TSApplicationException(builder.toString(), ex);

		}else {
			throw new TSSystemException(ex);
		}
	}

}
