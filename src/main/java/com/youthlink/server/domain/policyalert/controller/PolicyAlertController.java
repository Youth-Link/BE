package com.youthlink.server.domain.policyalert.controller;

import com.youthlink.server.common.apipayload.ApiResponse;
import com.youthlink.server.domain.policyalert.code.PolicyAlertSuccessCode;
import com.youthlink.server.domain.policyalert.dto.PolicyAlertReqDto;
import com.youthlink.server.domain.policyalert.dto.PolicyAlertResDto;
import com.youthlink.server.domain.policyalert.service.PolicyAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PolicyAlert", description = "정책 알림 구독 API")
@RestController
@RequestMapping("/api/policy-alerts")
@RequiredArgsConstructor
public class PolicyAlertController {

    private final PolicyAlertService policyAlertService;

    @Operation(summary = "구독 생성", description = "관심 키워드와 지역으로 정책 알림을 구독합니다")
    @PostMapping
    public ResponseEntity<ApiResponse<PolicyAlertResDto.AlertDetailDto>> createAlert(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PolicyAlertReqDto.CreateDto request) {
        return ResponseEntity.status(PolicyAlertSuccessCode.ALERT_CREATED.getStatus())
                .body(ApiResponse.onSuccess(PolicyAlertSuccessCode.ALERT_CREATED,
                        policyAlertService.createAlert(userDetails.getUsername(), request)));
    }

    @Operation(summary = "내 구독 목록 조회", description = "현재 사용자의 활성 정책 알림 구독 목록을 조회합니다")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PolicyAlertResDto.AlertDetailDto>>> getMyAlerts(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(PolicyAlertSuccessCode.ALERT_LIST_OK.getStatus())
                .body(ApiResponse.onSuccess(PolicyAlertSuccessCode.ALERT_LIST_OK,
                        policyAlertService.getMyAlerts(userDetails.getUsername())));
    }

    @Operation(summary = "구독 삭제", description = "정책 알림 구독을 비활성화합니다")
    @DeleteMapping("/{alertId}")
    public ResponseEntity<ApiResponse<Void>> deleteAlert(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long alertId) {
        policyAlertService.deleteAlert(userDetails.getUsername(), alertId);
        return ResponseEntity.status(PolicyAlertSuccessCode.ALERT_DELETED.getStatus())
                .body(ApiResponse.onSuccess(PolicyAlertSuccessCode.ALERT_DELETED, null));
    }
}
