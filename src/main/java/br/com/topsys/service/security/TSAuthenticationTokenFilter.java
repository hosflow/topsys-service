package br.com.topsys.service.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TSAuthenticationTokenFilter extends OncePerRequestFilter {

	private TSTokenService tokenService;

	public TSAuthenticationTokenFilter(TSTokenService tokenService) {
		this.tokenService = tokenService;
	}

	@SuppressWarnings("null")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var token = tokenService.getToken(request);
		var refreshToken = tokenService.getRefreshToken(request);
		

		try {

			if(request.getRequestURI().contains("refresh-token")) {
				
				this.authenticateWithToken(refreshToken);
				
			}else if (token != null) {

				this.tokenService.isTokenValid(token);
				this.authenticateWithToken(token);

			}

			filterChain.doFilter(request, response);
			
		} catch (Exception e) {
			handleException(response, e);
		}

		

	}

	private void authenticateWithToken(String token) {

		if (token != null) {
			var subject = tokenService.getSubject(token);

			var authentication = new UsernamePasswordAuthenticationToken(subject, null, null);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

	}

	private void handleException(HttpServletResponse response, Exception ex) throws IOException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
	}

}
