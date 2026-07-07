package com.youthlink.server.domain.member.dto;

import com.youthlink.server.domain.member.enums.EducationLevel;
import com.youthlink.server.domain.member.enums.Gender;
import lombok.Builder;

public class MemberResDto {

    @Builder
    public record MemberDetailDto(
            Long id,
            String email,
            String name,
            Integer age,
            Gender gender,
            RegionDto region,
            EducationLevel education,
            String employmentStatus,
            String incomeLevel,
            boolean isProfileComplete) {
    }

    @Builder
    public record RegionDto(
            Long id,
            String sido,
            String sigungu) {
    }
}
