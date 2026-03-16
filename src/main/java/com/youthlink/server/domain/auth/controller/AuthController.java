package com.youthlink.server.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youthlink.server.common.apipayload.ApiResponse;
import com.youthlink.server.common.security.code.AuthSuccessCode;
import com.youthlink.server.domain.auth.dto.AuthResDto;
import com.youthlink.server.domain.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급받습니다.")
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<AuthResDto.ReissueDto>> reissue(HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.status(AuthSuccessCode.REISSUE_SUCCESS.getStatus())
                .body(ApiResponse.onSuccess(AuthSuccessCode.REISSUE_SUCCESS, authService.reissue(request, response)));
    }

    @Operation(summary = "로그아웃", description = "리프레시 토큰 쿠키를 삭제하여 로그아웃 처리를 합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.status(AuthSuccessCode.LOGOUT_SUCCESS.getStatus())
                .body(ApiResponse.onSuccess(AuthSuccessCode.LOGOUT_SUCCESS, null));
    }

}
