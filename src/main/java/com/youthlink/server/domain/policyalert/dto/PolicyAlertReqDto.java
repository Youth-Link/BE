package com.youthlink.server.domain.policyalert.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PolicyAlertReqDto {

    public record CreateDto(
            @NotBlank(message = "키워드는 필수입니다")
            @Size(max = 50, message = "키워드는 50자 이하여야 합니다")
            String keyword,

            @Size(max = 50, message = "지역은 50자 이하여야 합니다")
            String region
    ) {}
}
