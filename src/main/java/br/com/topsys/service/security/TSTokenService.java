package br.com.topsys.service.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.topsys.base.exception.TSSystemException;
import br.com.topsys.base.model.TSSecurityModel;
import br.com.topsys.base.util.TSUtil;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TSTokenService {

	private static final String FLAG_REFRESH_TOKEN = "flagRefreshToken";

	@Value("${topsys.jwt.expiration}")
	private String expiration;

	@Value("${topsys.jwt.refresh.expiration}")
	private String refreshExpiration;

	@Value("${topsys.jwt.secret}")
	private String secret;

	@Autowired
	private HttpServletRequest httpRequest;

	public TSSecurityModel generateToken(Authentication authentication) {

		TSSecurityModel model = (TSSecurityModel) authentication.getPrincipal();
		try {

			model.setToken(generateToken(model));

		} catch (JWTCreationException exception) {
			throw new RuntimeException("Erro ao gerar o token jwt", exception);
		}

		return model;

	}

	public String generateToken(TSSecurityModel securityModel) {

		try {

			return generateToken(securityModel, expiracao(expiration));

		} catch (JWTCreationException exception) {
			throw new RuntimeException("Erro ao gerar o token jwt", exception);
		}

	}

	public String generateRefreshToken(TSSecurityModel securityModel) {
		try {

			return this.generateRefreshToken(securityModel, expiracao(refreshExpiration));

		} catch (JWTCreationException exception) {
			throw new RuntimeException("Erro ao gerar o refresh token jwt", exception);
		}
	}

	public String getSubject(String tokenJwt) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm).withIssuer("TopSys IT Solutions").build().verify(tokenJwt).getSubject();

		} catch (JWTVerificationException exception) {
			throw new RuntimeException("Token JWT inválido ou expirado!", exception);
		}
	}

	public String getClaim(String tokenJwt, String claim) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			var claimAux = JWT.require(algorithm).withIssuer("TopSys IT Solutions").build().verify(tokenJwt)
					.getClaim(claim);

			if (!claimAux.isNull()) {
				return claimAux.toString();
			}

		} catch (JWTVerificationException exception) {
			throw new RuntimeException("Token JWT inválido ou expirado!", exception);
		}

		return null;
	}

	public Map<String, Object> getClaims() {

		if (TSUtil.isEmpty(this.expiration) || TSUtil.isEmpty(this.getToken())) {
			return null;
		}

		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			var claimAux = JWT.require(algorithm).withIssuer("TopSys IT Solutions").build().verify(this.getToken())
					.getClaims();

			Map<String, Object> map = new HashMap<>();

			for (Entry<String, Claim> entry : claimAux.entrySet()) {
				map.put(entry.getKey(), entry.getValue().as(Object.class));
			}

			return map;

		} catch (JWTVerificationException exception) {
			throw new RuntimeException("Token JWT inválido ou expirado!", exception);
		}

	}

	public String getClaim(String claim) {
		return this.getClaim(this.getToken(), claim);
	}

	public String getToken(HttpServletRequest request) {

		var authorization = request.getHeader("Authorization");

		if (authorization != null) {
			return authorization.replace("Bearer ", "");

		}

		return null;

	}
	
	public String getRefreshToken(HttpServletRequest request) {

		return request.getHeader("refreshToken");

	}

	public String getToken() {

		return getToken(httpRequest);

	}

	public void isTokenValid(String token) {

		var decode = this.validToken(token);

		if (!decode.getClaim(FLAG_REFRESH_TOKEN).isMissing()) {
			throw new TSSystemException("Token inválido!");
		}

	}

	public Boolean isRefreshTokenValid(String token) {
		try {

			validToken(token);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private DecodedJWT validToken(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secret);

		return JWT.require(algorithm).withIssuer("TopSys IT Solutions").build().verify(token);
	}

	private Instant expiracao(String minutes) {

		return LocalDateTime.now().plusMinutes(Integer.parseInt(minutes)).toInstant(ZoneOffset.of("-03:00"));
	}

	private String generateToken(TSSecurityModel securityModel, Instant expiracao) {
		var algorithm = Algorithm.HMAC256(secret);

		return JWT.create().withIssuer("TopSys IT Solutions")
				.withSubject(securityModel.getLogin())
				.withClaim("id", securityModel.getId())
				.withClaim("usuarioFuncaoId", securityModel.getUsuarioFuncaoId())
				.withClaim("origemId", securityModel.getOrigemId())
				.withClaim("flagAdministrador", securityModel.isFlagAdministrador())
				.withIssuedAt(Instant.now()).withExpiresAt(expiracao)
				.sign(algorithm);
	}

	private String generateRefreshToken(TSSecurityModel securityModel, Instant expiracao) {
		var algorithm = Algorithm.HMAC256(secret);

		return JWT.create().withIssuer("TopSys IT Solutions")
				.withSubject(securityModel.getLogin())
				.withClaim("id", securityModel.getId())
				.withClaim("usuarioFuncaoId", securityModel.getUsuarioFuncaoId())
				.withClaim("origemId", securityModel.getOrigemId())
				.withClaim("flagAdministrador", securityModel.isFlagAdministrador())
				.withIssuedAt(Instant.now()).withExpiresAt(expiracao)
				.withClaim(FLAG_REFRESH_TOKEN, true).sign(algorithm);
	}

	public void decoderToken(TSSecurityModel securityModel) {

		String id = this.getClaim(securityModel.getRefreshToken(), "id");
		String origemId = this.getClaim(securityModel.getRefreshToken(), "origemId");
		String usuarioFuncaoId = this.getClaim(securityModel.getRefreshToken(), "usuarioFuncaoId");
		boolean flagAdministrador = Boolean.parseBoolean(this.getClaim(securityModel.getRefreshToken(), "flagAdministrador"));

		securityModel.setLogin(this.getSubject(securityModel.getRefreshToken()));

		if (!TSUtil.isEmpty(id)) {
			securityModel.setId(Long.valueOf(id));
		}

		if (!TSUtil.isEmpty(origemId)) {
			securityModel.setOrigemId(Long.valueOf(origemId));
		}

		if (!TSUtil.isEmpty(usuarioFuncaoId)) {
			securityModel.setUsuarioFuncaoId(Long.valueOf(usuarioFuncaoId));
		}
		
		if(flagAdministrador) {
			securityModel.setFlagAdministrador(flagAdministrador);
		}

	}

}
