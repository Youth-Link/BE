package com.youthlink.server.domain.region.controller;

import com.youthlink.server.common.apipayload.ApiResponse;
import com.youthlink.server.domain.member.dto.MemberResDto;
import com.youthlink.server.domain.region.code.RegionSuccessCode;
import com.youthlink.server.domain.region.converter.RegionConverter;
import com.youthlink.server.domain.region.entity.Region;
import com.youthlink.server.domain.region.repository.RegionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Region API", description = "지역 관련 API")
@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionRepository regionRepository;

    @Operation(summary = "전체 지역 목록 조회", description = "시/도, 시/군/구 전체 지역 목록을 조회합니다. 프로필 설정 시 드롭다운 구성에 사용합니다.")
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<MemberResDto.RegionDto>>> getAllRegions() {
        List<Region> regions = regionRepository.findAll();
        return ResponseEntity.status(RegionSuccessCode.REGION_FOUND.getStatus())
                .body(ApiResponse.onSuccess(RegionSuccessCode.REGION_FOUND, RegionConverter.toRegionDtoList(regions)));
    }
}
