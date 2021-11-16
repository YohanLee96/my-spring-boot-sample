package com.gsd.config.security.filter;

import com.blockware.gwonjidan.common.config.security.SecurityFilterUtil;
import com.blockware.gwonjidan.common.dto.LoginDTO;
import com.blockware.gwonjidan.common.dto.ResponseDTO;
import com.blockware.gwonjidan.common.error.BusinessException;
import com.blockware.gwonjidan.common.error.code.SecurityErrorCode;
import com.blockware.gwonjidan.common.error.security.UnknownException;
import com.blockware.gwonjidan.common.jwt.JWTUtil;
import com.blockware.gwonjidan.domain.user.model.User;
import com.gsd.component.jwt.JWTUtil;
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

    protected final JWTUtil jwtUtil;

    public LoginFilter(
            AuthenticationManager authenticationManager,
            JWTUtil jwtUtil) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtUtil = jwtUtil;
    }


    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        LoginDTO loginDTO = null;
        try {
            loginDTO = SecurityFilterUtil.toLoginDTO(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownException("알 수 없는 에러");
        }


        return getAuthentication(loginDTO);

    }


    private Authentication getAuthentication(LoginDTO loginDTO) {
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
        String accessToken = JWTUtil.BEARER+
                jwtUtil.generateAccessToken(user.getEmail(), user.getId(), SecurityFilterUtil.getAuthority(authResult.getAuthorities()));

        successResponse(response, new LoginDTO.LoginSuccess(accessToken, user.getName(), user.getUserType()));
    }

    protected void businessErrorReseponse(HttpServletResponse response, BusinessException ex) {
        response.setStatus(400);
        SecurityFilterUtil.jsonResponse(response, new ResponseDTO(ex.getErrorCode()));
    }

    protected void invalidAccountResponse(HttpServletResponse response) {
        response.setStatus(400);
        SecurityFilterUtil.jsonResponse(response,
                new ResponseDTO(SecurityErrorCode.INVALID_ACCOUNT_INFORMATION_MESSAGE));
    }

    protected void UnknownErrorResponse(HttpServletResponse response) {
        response.setStatus(400);
        SecurityFilterUtil.jsonResponse(response,
                new ResponseDTO(SecurityErrorCode.UNKNOWN_ERROR_MESSAGE));
    }

    protected void successResponse(HttpServletResponse response, LoginDTO.LoginSuccess loginSuccess) {
        SecurityFilterUtil.jsonResponse(response, new ResponseDTO(loginSuccess));
    }

}
