package com.gsd.global.security.filter;

import com.gsd.global.component.jwt.ClaimDTO;
import com.gsd.global.component.jwt.JWTTokenProvider;
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

    private final JWTTokenProvider jwtTokenProvider;

    public LoginCheckFilter(
            AuthenticationManager authenticationManager,
            JWTTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {


        String token = jwtTokenProvider.getTokenByHeader(request);
        if(token == null || !token.startsWith(JWTTokenProvider.BEARER)){
            chain.doFilter(request, response);
            return;
        }

        token = token.replaceFirst(JWTTokenProvider.BEARER, "");

        if(jwtTokenProvider.validToken(token)) {
            ClaimDTO claim = jwtTokenProvider.get(token);
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
