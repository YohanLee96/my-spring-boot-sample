package com.gsd.config.security.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // TODO: 2020-12-09 이메일 확인.
        // TODO: 2020-12-09 Redis에서 해당 정보 삭제처리.
        // TODO: 2020-12-09  Response 처리.
    }
}
