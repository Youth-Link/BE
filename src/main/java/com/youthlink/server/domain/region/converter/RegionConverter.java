package com.youthlink.server.domain.region.converter;

import com.youthlink.server.domain.member.dto.MemberResDto;
import com.youthlink.server.domain.region.entity.Region;

import java.util.List;
import java.util.stream.Collectors;

public class RegionConverter {

    public static MemberResDto.RegionDto toRegionDto(Region region) {
        return MemberResDto.RegionDto.builder()
                .id(region.getId())
                .sido(region.getSido())
                .sigungu(region.getSigungu())
                .build();
    }

    public static List<MemberResDto.RegionDto> toRegionDtoList(List<Region> regions) {
        return regions.stream()
                .map(RegionConverter::toRegionDto)
                .collect(Collectors.toList());
    }
}
