package com.youthlink.server.domain.policy.dto;

import lombok.Builder;

public class PolicyResDto {

    @Builder
    public record PolicySummaryDto(
            Long id,
            String bizId,
            String polyBizSjnm,
            String cnsgNmor,
            String ctpvNm,
            String ageInfo,
            String polyUrl
    ) {}

    @Builder
    public record PolicyDetailDto(
            Long id,
            String bizId,
            String polyBizSjnm,
            String polyItcnCn,
            String sporCn,
            String rqutPrdCn,
            String ageInfo,
            String empmSttsCd,
            String accrRqisCd,
            String incmRqisCn,
            String cnsgNmor,
            String polyUrl,
            String ctpvNm
    ) {}
}
