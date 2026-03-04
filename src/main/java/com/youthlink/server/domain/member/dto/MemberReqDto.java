package com.youthlink.server.domain.member.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class MemberReqDto {

    public record ProfileUpdateDto(
            @Size(max = 50, message = "이름은 50자 이하여야 합니다") String name,

            @Min(value = 1, message = "나이는 1 이상이어야 합니다") Integer age,

            @Size(max = 100, message = "지역은 100자 이하여야 합니다") String region,

            String education,
            String employmentStatus,
            String incomeLevel) {
    }
}
