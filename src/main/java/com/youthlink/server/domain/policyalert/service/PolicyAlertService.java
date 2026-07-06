package com.youthlink.server.domain.policyalert.service;

import com.youthlink.server.domain.policyalert.dto.PolicyAlertReqDto;
import com.youthlink.server.domain.policyalert.dto.PolicyAlertResDto;

import java.util.List;

public interface PolicyAlertService {
    PolicyAlertResDto.AlertDetailDto createAlert(String email, PolicyAlertReqDto.CreateDto request);
    List<PolicyAlertResDto.AlertDetailDto> getMyAlerts(String email);
    void deleteAlert(String email, Long alertId);
}
