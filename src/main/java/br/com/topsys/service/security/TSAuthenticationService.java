package br.com.topsys.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.topsys.base.model.TSSecurityModel;

@Service
public class TSAuthenticationService {

	@Autowired
	private TSTokenService tokenService;

	public TSSecurityModel authenticate(TSSecurityModel model) {

		if (model != null) {

			authenticateToken(model);

			String refreshToken = tokenService.generateRefreshToken(model);
			model.setRefreshToken(refreshToken);

		}

		return model;

	}

	public TSSecurityModel refreshToken(TSSecurityModel model) {

		if (model != null && this.tokenService.isRefreshTokenValid(model.getRefreshToken())) {
			
			this.tokenService.decoderToken(model);
			
			this.authenticate(model);

			return model;
		}

		return null;
	}

	private void authenticateToken(TSSecurityModel model) {

		var authenticationToken = new UsernamePasswordAuthenticationToken(model, model.getLogin());

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		model  = tokenService.generateToken(authenticationToken);

	}

}
