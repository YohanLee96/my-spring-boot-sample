package com.gsd.config.security;

import com.gsd.component.jwt.JWTUtil;
import com.gsd.config.security.filter.AdminLoginFilter;
import com.gsd.config.security.filter.AppLoginFilter;
import com.gsd.config.security.filter.LoginCheckFilter;
import com.gsd.config.security.filter.error.RestAccessDeniedHandler;
import com.gsd.config.security.filter.error.RestAuthenticationEntryPoint;
import com.gsd.domain.user.constant.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Created by Yohan lee
 * Created on 2021-11-17.
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final JWTUtil jwtUtil;
	
	private static final String ACADEMY_PACKAGE = "/api/admin/academy/**";
	
	private static final String WORK_PACKAGE = "/api/admin/work/**";
	
	private static final String TEACHER_PACKAGE = "/api/admin/teacher/**";
	
	private static final String STUDENT_PACKAGE = "/api/admin/student/**";
	
	private static final String COMMON_PACKAGE = "/api/v1/common/**";
	
	/**
	 * 정적 리소스 무시 처리.
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				"/v2/api-docs", "/swagger-resources/**",
				"/swagger-ui.html", "/webjars/**",
				"/swagger/**", "/image/**");
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
				antMatcher("/**").
				httpBasic().disable().
				formLogin().disable().
				cors().
				and().
				csrf().disable().
				addFilterBefore(loginCheckFilter(), BasicAuthenticationFilter.class).
				addFilterBefore(adminLoginFilter(), UsernamePasswordAuthenticationFilter.class).
				addFilterBefore(userLoginFilter(), UsernamePasswordAuthenticationFilter.class). //로그인 필터
				sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
				and().
				logout().
				logoutSuccessUrl("/api/logout").
				and().
				authorizeRequests(config -> {
					config.requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
					config.antMatchers(ACADEMY_PACKAGE)
							.hasAnyAuthority(UserType.SUPER_ADMIN.name());
					config.antMatchers(TEACHER_PACKAGE)
							.hasAnyAuthority(UserType.SUPER_ADMIN.name(), UserType.HEAD_TEACHER.name());
					config.antMatchers(WORK_PACKAGE, STUDENT_PACKAGE)
							.hasAnyAuthority(UserType.SUPER_ADMIN.name(), UserType.HEAD_TEACHER.name(), UserType.TEACHER.name());
					config.antMatchers(COMMON_PACKAGE).permitAll();
					config.antMatchers("/api/v1/sign/**").permitAll();
					config.antMatchers("/api/v1/**")
							.hasAnyAuthority(UserType.STUDENT.name());
					config.anyRequest().authenticated();
				}).
				exceptionHandling()
				.accessDeniedHandler(new RestAccessDeniedHandler())
				.authenticationEntryPoint(new RestAuthenticationEntryPoint())
		;
		
		
	}
	
	private UsernamePasswordAuthenticationFilter userLoginFilter() throws Exception {
		return new AppLoginFilter(authenticationManager(), jwtUtil);
	}
	
	private UsernamePasswordAuthenticationFilter adminLoginFilter() throws Exception {
		return new AdminLoginFilter(authenticationManager(), jwtUtil);
	}
	
	private BasicAuthenticationFilter loginCheckFilter() throws Exception {
		return new LoginCheckFilter(authenticationManager(), jwtUtil);
	}
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		//Orgin -> 모든 URL  CODRS 요청 허용.
		//Orgin -> 모든 Http Header CORS 요청 허용.
		//Orgin -> 모든 Http Method CORS 요청 허용.
		configuration.addAllowedOrigin("*");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		//모든 URL Path값에 적용.
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

