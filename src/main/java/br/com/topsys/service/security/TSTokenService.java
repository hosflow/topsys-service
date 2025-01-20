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

import br.com.topsys.base.model.TSSecurityModel;
import br.com.topsys.base.model.TSUserModel;
import br.com.topsys.base.util.TSUtil;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TSTokenService {

	@Value("${topsys.jwt.expiration}")
	private String expiration;

	@Value("${topsys.jwt.refresh.expiration}")
	private String refreshExpiration;

	@Value("${topsys.jwt.secret}")
	private String secret;

	@Autowired
	private HttpServletRequest httpRequest;

	public String generateToken(Authentication authentication) {

		TSSecurityModel model = (TSSecurityModel) authentication.getPrincipal();
		try {
			return generateToken(model);

		} catch (JWTCreationException exception) {
			throw new RuntimeException("Erro ao gerar o token jwt", exception);
		}

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

			return this.generateToken(securityModel, expiracao(refreshExpiration));

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

		if (TSUtil.isEmpty(this.expiration)) {
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

	public String getToken() {

		return getToken(httpRequest);

	}

	public Boolean isTokenValid(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);

			JWT.require(algorithm).withIssuer("TopSys IT Solutions").build().verify(token);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Instant expiracao(String minutes) {

		return LocalDateTime.now().plusMinutes(Integer.parseInt(minutes)).toInstant(ZoneOffset.of("-03:00"));
	}

	private String generateToken(TSSecurityModel securityModel, Instant expiracao) {
		var algorithm = Algorithm.HMAC256(secret);

		return JWT.create()
				.withIssuer("TopSys IT Solutions")
				.withSubject(securityModel.getLogin())
				.withClaim("id", securityModel.getId())
				.withClaim("usuarioFuncaoId", securityModel.getUsuarioFuncaoId())
				.withClaim("origemId", securityModel.getOrigemId())
				.withExpiresAt(expiracao)
				.sign(algorithm);
	}
	
	public void decoderToken(TSSecurityModel securityModel) {
		
		securityModel.setLogin(this.getSubject(securityModel.getRefreshToken()));
		securityModel.setId(Long.valueOf(this.getClaim(securityModel.getRefreshToken(), "id")));
		securityModel.setOrigemId(Long.valueOf(this.getClaim(securityModel.getRefreshToken(), "origemId")));
		securityModel.setUsuarioFuncaoId(Long.valueOf(this.getClaim(securityModel.getRefreshToken(), "usuarioFuncaoId")));
		
	}

}
