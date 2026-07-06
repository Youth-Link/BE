package com.youthlink.server.domain.region.code;

import com.youthlink.server.common.apipayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RegionSuccessCode implements BaseSuccessCode {

    REGION_FOUND(HttpStatus.OK, "REGION200", "지역 목록 조회가 완료되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
