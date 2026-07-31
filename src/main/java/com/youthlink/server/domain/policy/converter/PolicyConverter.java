package com.youthlink.server.domain.policy.converter;

import com.youthlink.server.domain.policy.dto.PolicyResDto;
import com.youthlink.server.domain.policy.dto.YouthPolicyResponse;
import com.youthlink.server.domain.policy.entity.Policy;

public class PolicyConverter {

    public static Policy toPolicy(YouthPolicyResponse.PolicyItem item) {
        return Policy.builder()
                .bizId(item.getPlcyNo())
                .polyBizSjnm(item.getPlcyNm())
                .polyItcnCn(item.getPlcyExplnCn())
                .sporCn(item.getPlcySprtCn())
                .rqutPrdCn(item.toRqutPrdCn())
                .ageInfo(item.toAgeInfo())
                .empmSttsCd(item.getJobCd())
                .accrRqisCd(item.getSchoolCd())
                .incmRqisCn(item.toIncmRqisCn())
                .cnsgNmor(item.toCnsgNmor())
                .polyUrl(item.toPolyUrl())
                .ctpvNm(item.toCtpvNm())
                .build();
    }

    public static PolicyResDto.PolicySummaryDto toSummaryDto(Policy policy) {
        return PolicyResDto.PolicySummaryDto.builder()
                .id(policy.getId())
                .bizId(policy.getBizId())
                .polyBizSjnm(policy.getPolyBizSjnm())
                .cnsgNmor(policy.getCnsgNmor())
                .ctpvNm(policy.getCtpvNm())
                .ageInfo(policy.getAgeInfo())
                .polyUrl(policy.getPolyUrl())
                .build();
    }

    public static PolicyResDto.PolicyDetailDto toDetailDto(Policy policy) {
        return PolicyResDto.PolicyDetailDto.builder()
                .id(policy.getId())
                .bizId(policy.getBizId())
                .polyBizSjnm(policy.getPolyBizSjnm())
                .polyItcnCn(policy.getPolyItcnCn())
                .sporCn(policy.getSporCn())
                .rqutPrdCn(policy.getRqutPrdCn())
                .ageInfo(policy.getAgeInfo())
                .empmSttsCd(policy.getEmpmSttsCd())
                .accrRqisCd(policy.getAccrRqisCd())
                .incmRqisCn(policy.getIncmRqisCn())
                .cnsgNmor(policy.getCnsgNmor())
                .polyUrl(policy.getPolyUrl())
                .ctpvNm(policy.getCtpvNm())
                .build();
    }
}
