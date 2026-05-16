package com.youthlink.server.domain.policy.code;

import com.youthlink.server.common.apipayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PolicyErrorCode implements BaseErrorCode {
    POLICY_NOT_FOUND(HttpStatus.NOT_FOUND, "POLICY404", "정책을 찾을 수 없습니다"),
    POLICY_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "POLICY500", "정책 데이터 수집에 실패했습니다"),
    POLICY_EMBED_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "POLICY500", "정책 임베딩에 실패했습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
