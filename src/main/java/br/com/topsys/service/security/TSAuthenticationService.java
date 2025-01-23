package br.com.topsys.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.topsys.base.model.TSSecurityModel;

@Service
public class TSAuthenticationService {

	@Autowired
	private TSTokenService tokenService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	public TSSecurityModel authenticate(TSSecurityModel model) {

		TSSecurityModel modelAux = null;
		
		if (model != null) {

			modelAux = authenticateToken(model);

			String refreshToken = tokenService.generateRefreshToken(modelAux);
			
			modelAux.setOrigemId(model.getOrigemId());
			modelAux.setSenha(null);
			modelAux.setRefreshToken(refreshToken);
			
		}

		return modelAux;

	}

	public TSSecurityModel refreshToken(TSSecurityModel model) {

		if (model != null && this.tokenService.isRefreshTokenValid(model.getRefreshToken())) {
			
			this.tokenService.decoderToken(model);
			
			model.setToken(tokenService.generateToken(model));
			
			model.setRefreshToken(tokenService.generateRefreshToken(model));
			
			return model;

		}

		return null;
	}

	private TSSecurityModel authenticateToken(TSSecurityModel model) {
				
		var authenticationToken = new UsernamePasswordAuthenticationToken(model, model.getSenha());
		
		Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
	
		return tokenService.generateToken(authentication);

	}

}
