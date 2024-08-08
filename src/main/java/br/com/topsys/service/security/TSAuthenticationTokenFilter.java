package br.com.topsys.service.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TSAuthenticationTokenFilter extends OncePerRequestFilter {

	private TSTokenService tokenService;

	private Class securityModel;
	
	public TSAuthenticationTokenFilter(TSTokenService tokenService, Class model) {
		this.tokenService = tokenService;
		this.securityModel = model;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var token = getToken(request);
		
		if(this.tokenService.isTokenValid(token).booleanValue()) {
			authenticateWithToken(token);
		}

		filterChain.doFilter(request, response);

	}

	private void authenticateWithToken(String token) {

		if (token != null) {
			var subject = tokenService.getSubject(token);

			var authentication = new UsernamePasswordAuthenticationToken(subject, null, null);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

	}
	
	/*
	private void authenticateWithToken(String token) {
		
		TSSecurityModel usuarioModel = this.tokenService.getUserModel(token, this.securityModel);
		
		if(usuarioModel != null) {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioModel,usuarioModel.getLogin(),null);
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		
		
	}*/

	private String getToken(HttpServletRequest request) {

		var authorization = request.getHeader("Authorization");

		if (authorization != null) {
			return authorization.replace("Bearer ", "");

		}

		return null;

	}


}
