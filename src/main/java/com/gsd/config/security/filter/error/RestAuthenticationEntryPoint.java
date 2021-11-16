package com.gsd.config.security.filter.error;

import com.blockware.gwonjidan.common.config.security.SecurityFilterUtil;
import com.blockware.gwonjidan.common.dto.ResponseDTO;
import com.blockware.gwonjidan.common.error.code.SecurityErrorCode;
import com.gsd.config.security.SecurityFilterUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 스프링 시큐리티 Exception의 최종 End-Point
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        unAuthorizedErrorResponse(response);
    }


    private void unAuthorizedErrorResponse(HttpServletResponse response) {
        SecurityFilterUtil.jsonResponse(response, new Object());
    }

}
