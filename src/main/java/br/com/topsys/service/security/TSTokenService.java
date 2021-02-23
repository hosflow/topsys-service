package br.com.topsys.service.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.topsys.base.exception.TSSystemException;
import br.com.topsys.base.model.TSSecurityModel;
import br.com.topsys.base.util.TSParseUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public final class TSTokenService {

	@Value("${topsys.jwt.expiration}")
	private String expiration;

	@Value("${topsys.jwt.secret}")
	private String secret;

	public String generateToken(Authentication authentication) {

		TSSecurityModel model = (TSSecurityModel) authentication.getPrincipal();

		ObjectMapper objectMapper = getObjectMapper();

		try {
			String usuarioJson = objectMapper.writeValueAsString(model);

			return Jwts.builder().setIssuer("TopSys IT Solutions").setClaims(Jwts.claims().setSubject(usuarioJson))
					.setIssuedAt(new Date())
					.setExpiration(new Date(new Date().getTime() + TSParseUtil.stringToLong(expiration)))
					.signWith(SignatureAlgorithm.HS256, this.secret).compact();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public Boolean isTokenValid(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TSSecurityModel getUserModel(String token, Class classe) {

		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();

		ObjectMapper objectMapper = getObjectMapper();

		TSSecurityModel userModel = null;
		try {
			userModel = (TSSecurityModel) objectMapper.readValue(claims.getSubject(), classe);

		} catch (Exception e) {
			e.printStackTrace();
			throw new TSSystemException(e);

		}

		return userModel;

	}

	private ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}

}
