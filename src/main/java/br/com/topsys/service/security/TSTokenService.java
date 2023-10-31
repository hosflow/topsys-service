package br.com.topsys.service.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;


import br.com.topsys.base.model.TSSecurityModel;

@Service
public class TSTokenService {

	@Value("${topsys.jwt.expiration}")
	private String expiration;

	@Value("${topsys.jwt.secret}")
	private String secret;

	/*
	 * public Boolean isTokenValid(String token) { try {
	 * JWT.parser().setSigningKey(this.secret).parseClaimsJws(token); return true; }
	 * catch (Exception e) { return false; } }
	 */

	public String generateToken(Authentication authentication) {

		TSSecurityModel model = (TSSecurityModel) authentication.getPrincipal();
		try {
			var algorithm = Algorithm.HMAC256(secret);

			return JWT.create().withIssuer("TopSys IT Solutions").withSubject(model.getLogin())
					.withClaim("id", model.getId()).withClaim("origemId", model.getOrigemId())
					.withClaim("token", model.getToken())
					// .withClaim("permissoes", "xxxxxx" )
					.withExpiresAt(expiracao(expiration)).sign(algorithm);

		} catch (JWTCreationException exception) {
			throw new RuntimeException("Erro ao gerar o token jwt", exception);
		}

	}

	/*
	 * public String generateRefreshToken(Usuario usuario) { try {
	 * 
	 * var algorithm = Algorithm.HMAC256(SECRET);
	 * 
	 * return JWT.create() .withIssuer("TopSys IT Solutions")
	 * .withSubject(model.getLogin()) .withClaim("id", model.getId())
	 * .withClaim("origemId", model.getOrigemId()) .withClaim("token",
	 * model.getToken()) .withExpiresAt(expiracao(expiration)) .sign(algorithm);
	 * 
	 * } catch (JWTCreationException exception){ throw new
	 * RuntimeException("Erro ao gerar o refresh token jwt", exception); } }
	 */

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
			var claimAux = JWT.require(algorithm).withIssuer("TopSys IT Solutions").build().verify(tokenJwt).getClaim(claim);

			if(!claimAux.isNull()) {
				return claimAux.toString();
			}
			
		} catch (JWTVerificationException exception) {
			throw new RuntimeException("Token JWT inválido ou expirado!", exception);
		}
		
		return null;
	}

	private Instant expiracao(String minutes) {

		return LocalDateTime.now().plusMinutes(Integer.getInteger(minutes)).toInstant(ZoneOffset.of("-03:00"));
	}

}
