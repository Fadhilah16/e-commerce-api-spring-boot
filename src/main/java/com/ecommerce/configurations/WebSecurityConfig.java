package com.ecommerce.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.security.AuthEntryPointJwt;
import com.ecommerce.security.AuthTokenFilter;
import com.ecommerce.security.JwtUtils;
import com.ecommerce.services.UserService.UserDetailsServiceImpl;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
	private JwtUtils jwtUtils;
	private UserDetailsServiceImpl userDetailsServiceImpl;
	private AuthEntryPointJwt authEntryPointJwt;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(authEntryPointJwt).and()
	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	.authorizeRequests().antMatchers("/api/auth/**").permitAll()
	.antMatchers("/api/products/**").permitAll()
	.antMatchers("/api/cart/**").permitAll()
	.antMatchers("/api/orders/**").permitAll().anyRequest().authenticated();
	  
	http.authenticationProvider(authenticationProvider());
	http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

	  return http.build();
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
	  return new AuthTokenFilter(jwtUtils,userDetailsServiceImpl );
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		 
		authProvider.setUserDetailsService(userDetailsServiceImpl);
		authProvider.setPasswordEncoder(passwordEncoder());
	 
		return authProvider;
	}


	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	  return authConfig.getAuthenticationManager();
	}
}
