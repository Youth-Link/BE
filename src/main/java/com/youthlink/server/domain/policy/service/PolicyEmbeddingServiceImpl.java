package com.youthlink.server.domain.policy.service;

import com.youthlink.server.domain.policy.code.PolicyErrorCode;
import com.youthlink.server.domain.policy.entity.Policy;
import com.youthlink.server.domain.policy.exception.PolicyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyEmbeddingServiceImpl implements PolicyEmbeddingService {

    private final VectorStore vectorStore;

    @Override
    public void embedPolicies(List<Policy> policies) {
        if (policies == null || policies.isEmpty()) return;

        List<Document> documents = policies.stream()
                .filter(p -> p.getBizId() != null)
                .map(this::toDocument)
                .filter(Objects::nonNull)
                .toList();

        if (documents.isEmpty()) return;

        try {
            vectorStore.add(documents);
            log.info("Chroma 임베딩 완료 - {}건", documents.size());
        } catch (Exception e) {
            log.error("Chroma 임베딩 실패: {}", e.getMessage());
            throw new PolicyException(PolicyErrorCode.POLICY_EMBED_FAILED);
        }
    }

    private Document toDocument(Policy policy) {
        try {
            String content = String.format("""
                    정책명: %s
                    정책소개: %s
                    지원내용: %s
                    신청기간: %s
                    나이조건: %s
                    주관기관: %s
                    지역: %s
                    """,
                    nullToEmpty(policy.getPolyBizSjnm()),
                    nullToEmpty(policy.getPolyItcnCn()),
                    nullToEmpty(policy.getSporCn()),
                    nullToEmpty(policy.getRqutPrdCn()),
                    nullToEmpty(policy.getAgeInfo()),
                    nullToEmpty(policy.getCnsgNmor()),
                    nullToEmpty(policy.getCtpvNm())
            );

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("policyId", String.valueOf(policy.getId()));
            metadata.put("policyName", nullToEmpty(policy.getPolyBizSjnm()));
            metadata.put("organization", nullToEmpty(policy.getCnsgNmor()));
            metadata.put("sourceUrl", nullToEmpty(policy.getPolyUrl()));
            metadata.put("region", nullToEmpty(policy.getCtpvNm()));
            metadata.put("ageCondition", nullToEmpty(policy.getAgeInfo()));
            metadata.put("education", nullToEmpty(policy.getAccrRqisCd()));
            metadata.put("employmentStatus", nullToEmpty(policy.getEmpmSttsCd()));
            metadata.put("incomeCondition", nullToEmpty(policy.getIncmRqisCn()));

            // bizId를 Document ID로 고정하여 Chroma upsert 처리
            return new Document(policy.getBizId(), content, metadata);
        } catch (Exception e) {
            log.warn("Document 변환 실패 (bizId={}): {}", policy.getBizId(), e.getMessage());
            return null;
        }
    }

    private String nullToEmpty(String value) {
        return value != null ? value : "";
    }
}
