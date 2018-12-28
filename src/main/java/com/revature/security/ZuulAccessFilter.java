package com.revature.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class ZuulAccessFilter extends GenericFilterBean {

	//@Autowired
	//private JwtConfig jwtConfig;
	
	public ZuulAccessFilter() {
	}
	

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String headerFraud = ((HttpServletRequest) request).getHeader("RPM_ZUUL_ACCESS_HEADER");
		if (headerFraud == null || !headerFraud.equals("Trevin is a meanie")) {
			System.out.println(headerFraud);
			/*
			 * In case of attempted fraud, make sure it's clear; so we can guarantee that
			 * the user will not be authenticated
			 */
			SecurityContextHolder.clearContext();
			return;
		} else {
			
/*			Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecret().getBytes()).parseClaimsJws(token)
					.getBody();
			String username = claims.getSubject();
			List<String> authorities = (List<String>) claims.get("authorities");

			authorities.forEach(String::toString);
			
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
					authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
*/			System.out.println("giving auth");
			Authentication auth = new AccessAuthenticationToken(headerFraud, "ROLE_ADMIN", new ArrayList<>());
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		
	}

}
