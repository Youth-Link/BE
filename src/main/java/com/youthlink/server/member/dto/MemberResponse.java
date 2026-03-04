package com.youthlink.server.member.dto;

import com.youthlink.server.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {

    private Long id;
    private String email;
    private String name;
    private Integer age;
    private String region;
    private String education;
    private String employmentStatus;
    private String incomeLevel;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .age(member.getAge())
                .region(member.getRegion())
                .education(member.getEducation())
                .employmentStatus(member.getEmploymentStatus())
                .incomeLevel(member.getIncomeLevel())
                .build();
    }
}
