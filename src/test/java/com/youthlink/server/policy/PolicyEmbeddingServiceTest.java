package com.youthlink.server.policy;

import com.youthlink.server.domain.policy.entity.Policy;
import com.youthlink.server.domain.policy.service.PolicyEmbeddingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PolicyEmbeddingServiceTest {

    @Mock
    private VectorStore vectorStore;

    @InjectMocks
    private PolicyEmbeddingServiceImpl policyEmbeddingService;

    @Test
    @DisplayName("Policy를 Document로 변환하여 VectorStore에 저장한다")
    void embedPoliciesCallsVectorStore() {
        Policy policy = createTestPolicy();

        policyEmbeddingService.embedPolicies(List.of(policy));

        verify(vectorStore).add(org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    @DisplayName("Document metadata에 용진의 PolicySource와 맞는 키가 포함된다")
    void documentMetadataKeysMatchPolicySource() {
        Policy policy = createTestPolicy();

        ArgumentCaptor<List<Document>> captor = ArgumentCaptor.forClass(List.class);
        policyEmbeddingService.embedPolicies(List.of(policy));
        verify(vectorStore).add(captor.capture());

        Document doc = captor.getValue().get(0);
        Map<String, Object> metadata = doc.getMetadata();

        assertThat(metadata).containsKey("policyId");
        assertThat(metadata).containsKey("policyName");
        assertThat(metadata).containsKey("organization");
        assertThat(metadata).containsKey("sourceUrl");
        assertThat(metadata).containsKey("region");
        assertThat(metadata).containsKey("ageCondition");
        assertThat(metadata).containsKey("education");
        assertThat(metadata).containsKey("employmentStatus");
        assertThat(metadata).containsKey("incomeCondition");
    }

    @Test
    @DisplayName("Document metadata 값이 Policy 필드와 정확히 매핑된다")
    void documentMetadataValuesMatchPolicyFields() {
        Policy policy = createTestPolicy();

        ArgumentCaptor<List<Document>> captor = ArgumentCaptor.forClass(List.class);
        policyEmbeddingService.embedPolicies(List.of(policy));
        verify(vectorStore).add(captor.capture());

        Document doc = captor.getValue().get(0);
        Map<String, Object> metadata = doc.getMetadata();

        assertThat(metadata.get("policyName")).isEqualTo("청년 일자리 도약 장려금");
        assertThat(metadata.get("organization")).isEqualTo("고용노동부");
        assertThat(metadata.get("region")).isEqualTo("전국");
        assertThat(metadata.get("sourceUrl")).isEqualTo("https://example.com");
    }

    @Test
    @DisplayName("Document ID는 bizId로 고정되어 Chroma upsert가 가능하다")
    void documentIdEqualsBizId() {
        Policy policy = createTestPolicy();

        ArgumentCaptor<List<Document>> captor = ArgumentCaptor.forClass(List.class);
        policyEmbeddingService.embedPolicies(List.of(policy));
        verify(vectorStore).add(captor.capture());

        Document doc = captor.getValue().get(0);
        assertThat(doc.getId()).isEqualTo("R2023101933648");
    }

    @Test
    @DisplayName("빈 목록을 넘기면 VectorStore를 호출하지 않는다")
    void emptyListDoesNotCallVectorStore() {
        policyEmbeddingService.embedPolicies(List.of());

        org.mockito.Mockito.verifyNoInteractions(vectorStore);
    }

    private Policy createTestPolicy() {
        return Policy.builder()
                .bizId("R2023101933648")
                .polyBizSjnm("청년 일자리 도약 장려금")
                .polyItcnCn("중소기업 취업 청년에게 장려금을 지원합니다")
                .sporCn("월 60만원, 최대 12개월 지원")
                .rqutPrdCn("2024.01.01 ~ 2024.12.31")
                .ageInfo("만 15세 ~ 34세")
                .empmSttsCd("미취업자")
                .accrRqisCd("제한없음")
                .incmRqisCn("중위소득 150% 이하")
                .cnsgNmor("고용노동부")
                .polyUrl("https://example.com")
                .ctpvNm("전국")
                .build();
    }
}
