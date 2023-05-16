package com.shop.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // ajax의 경우 http request header에 XMLHttpRequest 값이 세팅되는데 인증되지 않은 사용자의 경우 "Unauthorized" 에러 발생 따라서 로그인 페이지로 이동
        if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            response.sendRedirect("/member/login");
        }
    }

}