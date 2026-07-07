package com.youthlink.server.domain.member.dto;

import com.youthlink.server.domain.member.enums.EducationLevel;
import com.youthlink.server.domain.member.enums.Gender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MemberReqDto {

    public record ProfileUpdateDto(
            @NotBlank(message = "이름은 필수 입력 값입니다")
            @Size(max = 50, message = "이름은 50자 이하여야 합니다")
            String name,

            @NotNull(message = "나이는 필수 입력 값입니다")
            @Min(value = 1, message = "나이는 1 이상이어야 합니다")
            Integer age,

            @NotNull(message = "성별은 필수 입력 값입니다")
            Gender gender,

            @NotNull(message = "지역 ID는 필수입니다")
            Long regionId,

            @NotNull(message = "학력 정보는 필수 입력 값입니다")
            EducationLevel education,

            @NotBlank(message = "고용 상태는 필수 입력 값입니다")
            @Size(max = 50, message = "고용 상태는 50자 이하여야 합니다")
            String employmentStatus,

            @NotBlank(message = "소득 수준은 필수 입력 값입니다")
            @Size(max = 50, message = "소득 수준은 50자 이하여야 합니다")
            String incomeLevel) {
    }
}
