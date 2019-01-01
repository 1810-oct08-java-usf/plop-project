package com.revature.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This currently checks if the current User is allowed to access a certain end point
 * using their role.
 *
 * @author Sean Doyle (1810-Oct22-Java-USF)
 * @author Josh Jibilian (1810-Oct22-Java-USF)
 * @author Colt Ossoff (1810-Oct22-Java-USF)
 * @author Bronwen Hughes (1810-Oct22-Java-USF)
 * @author John Savath (1810-Oct22-Java-USF)
 */

@EnableWebSecurity
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter{
	
	private ZuulConfig zuulConfig;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			/*
			 * Disables the protection against Cross-Site Request Forgery (CSRF), otherwise
			 * requests cannot be made to this request from the zuul-service.
			 */
			.csrf().disable()
	
			/*
			 * Ensure that a stateless session is used; session will not be used to store
			 * user information/state.
			 */
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	
			/*
			 * Handle any exceptions thrown during authentication by sending a response
			 * status of Authorized (401).
			 */
			.exceptionHandling()
			.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
			
			/*
			 * Adding our customized filter to check for the presence of the Zuul header or if that request is
			 * to get information from the Acutator
			 */
            .addFilterBefore(new CustomAuthenticationFilter(zuulConfig), UsernamePasswordAuthenticationFilter.class)
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
			.antMatchers(HttpMethod.GET, "/actuator/health").permitAll()
			.antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
			.antMatchers(HttpMethod.GET, "/actuator/routes").permitAll()
			
			// Allow unrestricted access to swagger's documentation
			.antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
			// All other requests must be authenticated
			.anyRequest().authenticated();
	}
	
	@Bean
	public ZuulConfig zuulConfig() {
		return new ZuulConfig();
	}
	@Autowired
	public void setZuulConfig(ZuulConfig zuulConfig) {
		this.zuulConfig = zuulConfig;
	}
}

