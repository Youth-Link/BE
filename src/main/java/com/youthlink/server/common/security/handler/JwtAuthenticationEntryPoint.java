package com.youthlink.server.common.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youthlink.server.common.apipayload.ApiResponse;
import com.youthlink.server.common.security.code.AuthErrorCode;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        // 인증 실패 시 401 에러를 JSON으로 내려줌 (간소화 버전)
        // 필터 단계에서는 ApiResponse 같은 객체를 직접 리턴할 수 없으므로 직접 JSON 스트링을 작성합니다.
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse<Object> apiResponse = ApiResponse.onFailure(AuthErrorCode.UNAUTHORIZED, null);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
