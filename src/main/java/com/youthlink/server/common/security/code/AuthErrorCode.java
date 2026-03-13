package com.youthlink.server.common.security.code;

import com.youthlink.server.common.apipayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {
    UNSUPPORTED_SOCIAL_PROVIDER(HttpStatus.BAD_REQUEST, "AUTH400", "지원하지 않는 소셜 로그인 서비스입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH403", "접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
