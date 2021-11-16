package com.gsd.config.security.filter;

import com.blockware.gwonjidan.common.config.security.SecurityFilterUtil;
import com.blockware.gwonjidan.common.jwt.JWTUtil;
import com.blockware.gwonjidan.domain.user.constant.UserType;
import com.gsd.component.jwt.JWTUtil;
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
            JWTUtil jwtUtil) {
        super(authenticationManager, jwtUtil);
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
