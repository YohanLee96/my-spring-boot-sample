package com.gsd.global.security.filter;


import com.gsd.global.component.dto.ApiResult;
import com.gsd.global.component.jwt.JWTTokenProvider;
import com.gsd.global.error.exception.BusinessException;
import com.gsd.global.error.exception.security.UnknownException;
import com.gsd.global.security.LoginDTO;
import com.gsd.global.security.SecurityErrorCode;
import com.gsd.global.security.SecurityFilterUtil;
import com.gsd.domain.user.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class LoginFilter extends UsernamePasswordAuthenticationFilter {

    protected final JWTTokenProvider jwtTokenProvider;

    public LoginFilter(
            AuthenticationManager authenticationManager,
            JWTTokenProvider jwtTokenProvider) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        LoginDTO.Request loginDTO = null;
        try {
            loginDTO = SecurityFilterUtil.toLoginDTO(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownException("알 수 없는 에러");
        }


        return getAuthentication(loginDTO);

    }


    private Authentication getAuthentication(LoginDTO.Request loginDTO) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(),
                loginDTO.getPassword(),
                null);
        return super.getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected abstract void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException;

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        if(failed instanceof UnknownException) {
            UnknownErrorResponse(response);
            return;
        }

        if(failed.getCause() instanceof BusinessException) {
            businessErrorReseponse(response, (BusinessException)failed.getCause());
        }

        invalidAccountResponse(response);

    }

    /**
     * 로그인 성공 시,  Request Header에 JWT 토큰
     * @param authResult 인증 결과
     * @param response 서블릿 Response 객체
     */
    protected void successLogin(Authentication authResult, HttpServletResponse response) {
        User user = (User)authResult.getPrincipal();
        String accessToken =
                jwtTokenProvider.generateAccessToken(
                        user.getUserEmail(),
                        user.getId(),
                        SecurityFilterUtil.getAuthority(authResult.getAuthorities())
                );

        successResponse(
                response,
                LoginDTO.LoginSuccess(JWTTokenProvider.BEARER +accessToken, user)
        );
    }

    protected void businessErrorReseponse(HttpServletResponse response, BusinessException ex) {
        response.setStatus(400);
        SecurityFilterUtil.jsonResponse(response, ApiResult.error(ex));
    }

    protected void invalidAccountResponse(HttpServletResponse response) {
        response.setStatus(400);
        SecurityFilterUtil.jsonResponse(
                response,
                ApiResult.error(SecurityErrorCode.INVALID_ACCOUNT_INFORMATION_MESSAGE)
        );
    }

    protected void UnknownErrorResponse(HttpServletResponse response) {
        response.setStatus(400);
        SecurityFilterUtil.jsonResponse(response, ApiResult.error(SecurityErrorCode.UNKNOWN_ERROR_MESSAGE));
    }

    protected void successResponse(HttpServletResponse response, LoginDTO.LoginSuccess loginSuccess) {
        SecurityFilterUtil.jsonResponse(response, ApiResult.success(loginSuccess));
    }

}
