package com.youthlink.server.domain.policy.controller;

import com.youthlink.server.common.apipayload.ApiResponse;
import com.youthlink.server.domain.policy.code.PolicyErrorCode;
import com.youthlink.server.domain.policy.code.PolicySuccessCode;
import com.youthlink.server.domain.policy.converter.PolicyConverter;
import com.youthlink.server.domain.policy.dto.PolicyResDto;
import com.youthlink.server.domain.policy.entity.Policy;
import com.youthlink.server.domain.policy.exception.PolicyException;
import com.youthlink.server.domain.policy.repository.PolicyRepository;
import com.youthlink.server.domain.policy.scheduler.DataPipelineScheduler;
import com.youthlink.server.domain.policy.service.PolicyEmbeddingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Policy", description = "청년 정책 API")
@RestController
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyRepository policyRepository;
    private final DataPipelineScheduler dataPipelineScheduler;
    private final PolicyEmbeddingService policyEmbeddingService;

    @Operation(summary = "정책 목록 조회", description = "수집된 청년 정책 전체 목록을 조회합니다")
    @GetMapping("/api/policies")
    public ResponseEntity<ApiResponse<List<PolicyResDto.PolicySummaryDto>>> getPolicies() {
        List<PolicyResDto.PolicySummaryDto> result = policyRepository.findAll().stream()
                .map(PolicyConverter::toSummaryDto)
                .toList();
        return ResponseEntity.status(PolicySuccessCode.POLICY_LIST_OK.getStatus())
                .body(ApiResponse.onSuccess(PolicySuccessCode.POLICY_LIST_OK, result));
    }

    @Operation(summary = "정책 상세 조회", description = "정책 ID로 청년 정책 상세 내용을 조회합니다")
    @GetMapping("/api/policies/{policyId}")
    public ResponseEntity<ApiResponse<PolicyResDto.PolicyDetailDto>> getPolicy(
            @PathVariable Long policyId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new PolicyException(PolicyErrorCode.POLICY_NOT_FOUND));
        return ResponseEntity.status(PolicySuccessCode.POLICY_DETAIL_OK.getStatus())
                .body(ApiResponse.onSuccess(PolicySuccessCode.POLICY_DETAIL_OK,
                        PolicyConverter.toDetailDto(policy)));
    }

    @Operation(summary = "정책 수동 동기화", description = "온통청년 API에서 정책을 즉시 수집하고 Chroma에 임베딩합니다")
    @PostMapping("/api/admin/policies/sync")
    public ResponseEntity<ApiResponse<Void>> syncPolicies() {
        dataPipelineScheduler.syncManually();
        return ResponseEntity.status(PolicySuccessCode.POLICY_SYNC_OK.getStatus())
                .body(ApiResponse.onSuccess(PolicySuccessCode.POLICY_SYNC_OK, null));
    }

    @Operation(summary = "단일 정책 Chroma 재적재",
            description = "특정 정책 1건을 Chroma에 다시 임베딩합니다. 전체 파이프라인 없이 특정 정책만 수정된 경우 사용합니다.")
    @PostMapping("/api/admin/policies/{policyId}/embed")
    public ResponseEntity<ApiResponse<Void>> reEmbedPolicy(@PathVariable Long policyId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new PolicyException(PolicyErrorCode.POLICY_NOT_FOUND));
        policyEmbeddingService.embedPolicies(List.of(policy));
        return ResponseEntity.status(PolicySuccessCode.POLICY_EMBED_OK.getStatus())
                .body(ApiResponse.onSuccess(PolicySuccessCode.POLICY_EMBED_OK, null));
    }
}
