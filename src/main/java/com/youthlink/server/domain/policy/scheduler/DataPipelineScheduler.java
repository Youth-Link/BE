package com.youthlink.server.domain.policy.scheduler;

import com.youthlink.server.domain.policy.entity.Policy;
import com.youthlink.server.domain.policy.service.PolicyEmbeddingService;
import com.youthlink.server.domain.policy.service.PolicyFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataPipelineScheduler {

    private final PolicyFetchService policyFetchService;
    private final PolicyEmbeddingService policyEmbeddingService;

    /**
     * 매일 새벽 2시에 온통청년 정책 데이터를 수집하고 Chroma에 임베딩한다.
     * 신규/변경된 정책만 반환받아 알림 매칭에 활용한다.
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void fetchAndEmbed() {
        log.info("정책 데이터 파이프라인 시작");
        try {
            List<Policy> changedPolicies = policyFetchService.fetchAndSave();
            policyEmbeddingService.embedPolicies(changedPolicies);
            log.info("정책 데이터 파이프라인 완료 - 처리 {}건", changedPolicies.size());
        } catch (Exception e) {
            log.error("정책 데이터 파이프라인 실패: {}", e.getMessage());
        }
    }

    /**
     * 수동 동기화 트리거 (POST /api/admin/policies/sync 에서 호출)
     */
    public List<Policy> syncManually() {
        log.info("정책 수동 동기화 시작");
        List<Policy> changedPolicies = policyFetchService.fetchAndSave();
        policyEmbeddingService.embedPolicies(changedPolicies);
        log.info("정책 수동 동기화 완료 - 처리 {}건", changedPolicies.size());
        return changedPolicies;
    }
}
