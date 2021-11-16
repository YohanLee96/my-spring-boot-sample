package com.gsd.config.security.filter;

import com.blockware.gwonjidan.common.jwt.ClaimDTO;
import com.blockware.gwonjidan.common.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class LoginCheckFilter extends BasicAuthenticationFilter {

    private final JWTUtil jwtUtil;

    public LoginCheckFilter(
            AuthenticationManager authenticationManager,
            JWTUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {


        String token = jwtUtil.getTokenByHeader(request);
        if(token == null || !token.startsWith(JWTUtil.BEARER)){
            chain.doFilter(request, response);
            return;
        }

        token = token.replaceFirst(JWTUtil.BEARER, "");

        if(jwtUtil.validToken(token)) {
            ClaimDTO claim = jwtUtil.get(token);
            if(claim != null) {
                SecurityContextHolder.getContext().setAuthentication(getAuthentication(claim));
            }
        }

        super.doFilterInternal(request, response, chain);
    }

    private Authentication getAuthentication(ClaimDTO claimDTO) {
        return new  UsernamePasswordAuthenticationToken(
                claimDTO.getSubject(),
                null,
                Collections.singletonList(claimDTO.getUserType())
        );

    }


}
