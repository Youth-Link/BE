package com.youthlink.server.member.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileUpdateRequest {

    @Size(max = 50, message = "이름은 50자 이하여야 합니다")
    private String name;

    @Min(value = 1, message = "나이는 1 이상이어야 합니다")
    private Integer age;

    @Size(max = 100, message = "지역은 100자 이하여야 합니다")
    private String region;

    private String education;

    private String employmentStatus;

    private String incomeLevel;
}
