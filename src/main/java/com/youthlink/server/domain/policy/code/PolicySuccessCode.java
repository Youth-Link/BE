package com.youthlink.server.domain.policy.code;

import com.youthlink.server.common.apipayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PolicySuccessCode implements BaseSuccessCode {
    POLICY_LIST_OK(HttpStatus.OK, "POLICY200", "정책 목록 조회 성공"),
    POLICY_DETAIL_OK(HttpStatus.OK, "POLICY200", "정책 상세 조회 성공"),
    POLICY_SYNC_OK(HttpStatus.OK, "POLICY200", "정책 동기화 성공"),
    POLICY_EMBED_OK(HttpStatus.OK, "POLICY200", "정책 임베딩 성공");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
