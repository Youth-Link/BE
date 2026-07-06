package com.youthlink.server.domain.policyalert.dto;

import lombok.Builder;

public class PolicyAlertResDto {

    @Builder
    public record AlertDetailDto(
            Long id,
            String keyword,
            String region,
            boolean active
    ) {}
}
