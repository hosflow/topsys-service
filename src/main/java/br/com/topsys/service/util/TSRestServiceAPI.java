package br.com.topsys.service.util;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.topsys.base.exception.TSApplicationException;
import br.com.topsys.base.exception.TSSystemException;
import br.com.topsys.base.model.TSRestModel;
import br.com.topsys.base.util.TSUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
public final class TSRestServiceAPI {

	private static final String NAO_PODE_SER_NULO = "O objeto passado por parâmetro do método post não pode ser nulo!";
	
	private RestTemplate restTemplate;

	public TSRestServiceAPI() {
		this.restTemplate = new RestTemplate();
	}
	

	public <T> T post(Class<T> classe, TSRestModel restModel) {

		T retorno = null;

		try {
	
			retorno = postForObject(classe, restModel, restModel.getModel());

			retorno = convertObject(classe, retorno);

		} catch (Exception e) {
			this.handlerException(e);
		}

		return retorno;

	}


	public <T> List<T> postList(Class<T> classe, TSRestModel restModel) {

		List<T> retorno = null;

		try {
		
			retorno = postForList(restModel, restModel.getModel());

			retorno = convertList(classe, retorno);

		} catch (Exception e) {
			this.handlerException(e);
		}

		return retorno;

	}

	public <T> T get(Class<T> classe, TSRestModel restModel) {

		T retorno = null;

		try {
			retorno = getForObject(classe, restModel);

			retorno = convertObject(classe, retorno);

		} catch (Exception e) {
			this.handlerException(e);
		}

		return retorno;

	}

	public <T> List<T> getList(Class<T> classe, TSRestModel restModel) {

		List<T> retorno = null;

		try {

			retorno = getForList(restModel);

			retorno = convertList(classe, retorno);

		} catch (Exception e) {
			this.handlerException(e);
		}

		return retorno;

	}
	
	private <T> T postForObject(Class<T> classe, TSRestModel restModel, Object model) {
		
		HttpEntity<Object> entity = getHttpEntity(model, restModel.getToken());

		return restTemplate.postForObject(restModel.getBaseUrl() + restModel.getUrl(), entity, classe);
	
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> postForList(TSRestModel restModel, Object model) {

		HttpEntity<Object> entity = getHttpEntity(model, restModel.getToken());

		return restTemplate.postForObject(restModel.getBaseUrl() + restModel.getUrl(), entity, List.class);

	}

	private <T> T getForObject(Class<T> classe, TSRestModel restModel) {

		return restTemplate.getForObject(restModel.getBaseUrl() + restModel.getUrl(), classe);

	}

	@SuppressWarnings("unchecked")
	private <T> List<T> getForList(TSRestModel restModel) {

		return restTemplate.getForObject(restModel.getBaseUrl() + restModel.getUrl(), List.class);

	}

	private HttpEntity<Object> getHttpEntity(Object object, String token) {

		if (TSUtil.isEmpty(object)) {
			throw new TSSystemException(NAO_PODE_SER_NULO);
		}

		HttpEntity<Object> entity;

		if (!TSUtil.isEmpty(token)) {

			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(token);
			entity = new HttpEntity<>(object, headers);

		} else {
			entity = new HttpEntity<>(object);
		}

		return entity;
	}


	private ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		
		return objectMapper; 
	}

	private <T> T convertObject(Class<T> classe, T retorno) {

		if (!TSUtil.isEmpty(retorno)) {

			retorno = getObjectMapper().convertValue(retorno, classe);
		}

		return retorno;
	}

	private <T> List<T> convertList(Class<T> classe, List<T> retorno) {

		if (!TSUtil.isEmpty(retorno)) {

			ObjectMapper objectMapper = getObjectMapper();

			CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, classe);

			retorno = objectMapper.convertValue(retorno, listType);
		}

		return retorno;
	}

	private void handlerException(Exception e) {
		String erroInterno = "Ocorreu um erro interno, entre em contato com a TI!";

		if (e instanceof TSApplicationException) {

			throw new TSApplicationException(e.getMessage(), ((TSApplicationException) e).getTSType());

		} else {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new TSSystemException(erroInterno, e);
		}

	}
	

}
