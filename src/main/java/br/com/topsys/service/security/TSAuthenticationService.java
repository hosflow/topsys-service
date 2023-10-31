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
				
		UsernamePasswordAuthenticationToken authenticationToken = null;

		if (model != null) {
			authenticationToken = new UsernamePasswordAuthenticationToken(model.getLogin(), null);

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			
			String token = tokenService.generateToken(authenticationToken);
				
			model.setToken(token);
			
		}
		 
		return model;
		
	}

}
