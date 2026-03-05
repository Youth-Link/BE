package com.youthlink.server.domain.member.dto;

import lombok.Builder;

public class MemberResDto {

    @Builder
    public record MemberDetailDto(
            Long id,
            String email,
            String name,
            Integer age,
            String region,
            String education,
            String employmentStatus,
            String incomeLevel) {
    }
}
