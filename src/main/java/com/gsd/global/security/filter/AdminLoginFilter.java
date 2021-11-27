package com.gsd.global.security.filter;


import com.gsd.global.component.jwt.JWTTokenProvider;
import com.gsd.global.security.SecurityFilterUtil;
import com.gsd.domain.user.constant.UserType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AdminLoginFilter extends LoginFilter {


    public AdminLoginFilter(
            AuthenticationManager authenticationManager,
            JWTTokenProvider jwtTokenProvider) {
        super(authenticationManager, jwtTokenProvider);
        setFilterProcessesUrl("/api/admin/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        UserType userType = SecurityFilterUtil.getAuthority(authResult.getAuthorities());

        if(userType.isAdminAuthority()) {
            successLogin(authResult, response);
        } else {
            invalidAccountResponse(response);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
