package com.youthlink.server.domain.policyalert.converter;

import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.policyalert.dto.PolicyAlertReqDto;
import com.youthlink.server.domain.policyalert.dto.PolicyAlertResDto;
import com.youthlink.server.domain.policyalert.entity.PolicyAlert;

public class PolicyAlertConverter {

    public static PolicyAlert toPolicyAlert(Member member, PolicyAlertReqDto.CreateDto request) {
        return PolicyAlert.builder()
                .member(member)
                .keyword(request.keyword())
                .region(request.region())
                .build();
    }

    public static PolicyAlertResDto.AlertDetailDto toAlertDetailDto(PolicyAlert alert) {
        return PolicyAlertResDto.AlertDetailDto.builder()
                .id(alert.getId())
                .keyword(alert.getKeyword())
                .region(alert.getRegion())
                .active(alert.isActive())
                .build();
    }
}
