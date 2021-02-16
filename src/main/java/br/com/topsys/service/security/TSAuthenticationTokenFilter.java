package br.com.topsys.service.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.topsys.base.model.TSSecurityModel;
import br.com.topsys.base.util.TSUtil;

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
		
		String token = recuperarToken(request);
		
		if(this.tokenService.isTokenValido(token)) {
			autenticarWithToken(token);
		}

		filterChain.doFilter(request, response);

	}
	
	private void autenticarWithToken(String token) {
		
		TSSecurityModel usuarioModel = this.tokenService.getUsuarioModel(token, this.securityModel);
		
		if(usuarioModel != null) {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioModel,usuarioModel.getLogin(),null);
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		
		
	}

	private String recuperarToken(HttpServletRequest request) {

		String token = request.getHeader("Authorization");
		
		if (TSUtil.isEmpty(token) || !token.startsWith("Bearer ")) {
			return null;
		}

		return token.substring(7, token.length());

	}

}
