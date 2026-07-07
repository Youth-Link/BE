package com.youthlink.server.domain.member.converter;

import com.youthlink.server.domain.member.dto.MemberResDto;
import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.region.entity.Region;

public class MemberConverter {

    public static MemberResDto.MemberDetailDto toMemberDetailDto(Member member) {
        return MemberResDto.MemberDetailDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .age(member.getAge())
                .gender(member.getGender())
                .region(toRegionDto(member.getRegion()))
                .education(member.getEducation())
                .employmentStatus(member.getEmploymentStatus())
                .incomeLevel(member.getIncomeLevel())
                .isProfileComplete(member.isProfileComplete())
                .build();
    }

    private static MemberResDto.RegionDto toRegionDto(Region region) {
        if (region == null) {
            return null;
        }
        return MemberResDto.RegionDto.builder()
                .id(region.getId())
                .sido(region.getSido())
                .sigungu(region.getSigungu())
                .build();
    }
}
