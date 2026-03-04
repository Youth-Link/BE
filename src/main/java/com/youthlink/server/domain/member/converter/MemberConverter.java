package com.youthlink.server.domain.member.converter;

import com.youthlink.server.domain.member.dto.MemberResDto;
import com.youthlink.server.domain.member.entity.Member;

public class MemberConverter {

    public static MemberResDto.MemberDetailDto toMemberDetailDto(Member member) {
        return MemberResDto.MemberDetailDto.builder()
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
