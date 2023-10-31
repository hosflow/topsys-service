package br.com.topsys.service.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class TSAuthenticationTokenFilter extends OncePerRequestFilter {

	private TSTokenService tokenService;

	public TSAuthenticationTokenFilter(TSTokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var token = getToken(request);

		authenticateWithToken(token);

		filterChain.doFilter(request, response);

	}

	private void authenticateWithToken(String token) {

		if (token != null) {
			var subject = tokenService.getSubject(token);

			var authentication = new UsernamePasswordAuthenticationToken(subject, null, null);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

	}

	private String getToken(HttpServletRequest request) {

		var authorization = request.getHeader("Authorization");

		if (authorization != null) {
			return authorization.replace("Bearer ", "");

		}

		return null;

	}

}
