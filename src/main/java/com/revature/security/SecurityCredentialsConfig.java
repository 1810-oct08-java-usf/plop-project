package com.revature.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//public class SecurityCredentialsConfig {}

@EnableWebSecurity
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("jank");
		http
			.csrf().disable()
			.headers().frameOptions().disable().and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.exceptionHandling()
			.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
			//.addFilter(new BasicAuthenticationFilter(authenticationManager()))
			//.addFilterAfter(new ZuulAccessFilter(), GenericFilterBean.class)
			
            .addFilterBefore(new CustomAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
			
			// Allow POST requests to the "/auth" and "/auth/users" endpoints
			.mvcMatchers(HttpMethod.POST, "/auth").permitAll()
			.mvcMatchers(HttpMethod.POST, "/project").permitAll()

			// Allow only admins to access the h2-console
			.mvcMatchers("/h2-console/**").hasRole("ADMIN")
			
			/*
			 * Allow unrestricted access to the actuator/info endpoint. Otherwise, AWS ELB
			 * cannot perform a health check on the instance and it drains the instances.
			 */

			.antMatchers(HttpMethod.GET, "/actuator/info").permitAll()
			.antMatchers(HttpMethod.GET, "/actuator/routes").permitAll()
			
			// Allow unrestricted access to swagger's documentation
			.antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
			// All other requests must be authenticated
			.anyRequest().authenticated();
	}
	
}

