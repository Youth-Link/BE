package com.youthlink.server.domain.policy.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.youthlink.server.domain.policy.code.PolicyErrorCode;
import com.youthlink.server.domain.policy.config.YouthPolicyApiProperties;
import com.youthlink.server.domain.policy.converter.PolicyConverter;
import com.youthlink.server.domain.policy.dto.YouthPolicyResponse;
import com.youthlink.server.domain.policy.entity.Policy;
import com.youthlink.server.domain.policy.exception.PolicyException;
import com.youthlink.server.domain.policy.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyFetchServiceImpl implements PolicyFetchService {

    private static final XmlMapper XML_MAPPER = new XmlMapper();
    private static final int PAGE_SIZE = 100;

    private final RestClient youthPolicyRestClient;
    private final PolicyRepository policyRepository;
    private final YouthPolicyApiProperties props;

    @Override
    public List<Policy> fetchAndSave() {
        List<Policy> changedPolicies = new ArrayList<>();
        int page = 1;

        while (true) {
            List<YouthPolicyResponse.PolicyItem> items = fetchPage(page);
            if (items == null || items.isEmpty()) break;

            List<Policy> pageResult = saveAll(items);
            changedPolicies.addAll(pageResult);

            if (items.size() < PAGE_SIZE) break;
            page++;
        }

        log.info("정책 동기화 완료 - 신규/변경 {}건", changedPolicies.size());
        return changedPolicies;
    }

    private List<YouthPolicyResponse.PolicyItem> fetchPage(int pageIndex) {
        try {
            int finalPageIndex = pageIndex;
            String xml = youthPolicyRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("openApiVlak", props.getKey())
                            .queryParam("pageIndex", finalPageIndex)
                            .queryParam("pageSize", PAGE_SIZE)
                            .build())
                    .retrieve()
                    .body(String.class);

            YouthPolicyResponse response = XML_MAPPER.readValue(xml, YouthPolicyResponse.class);
            return response.getYouthPolicies();
        } catch (Exception e) {
            log.error("온통청년 API 호출 실패 (page={}): {}", pageIndex, e.getMessage());
            throw new PolicyException(PolicyErrorCode.POLICY_FETCH_FAILED);
        }
    }

    @Transactional
    public List<Policy> saveAll(List<YouthPolicyResponse.PolicyItem> items) {
        List<Policy> changed = new ArrayList<>();

        for (YouthPolicyResponse.PolicyItem item : items) {
            if (item.getBizId() == null || item.getBizId().isBlank()) continue;

            Optional<Policy> existing = policyRepository.findByBizId(item.getBizId());
            if (existing.isPresent()) {
                Policy policy = existing.get();
                boolean isChanged = policy.update(item);
                if (isChanged) {
                    changed.add(policy);
                }
            } else {
                Policy saved = policyRepository.save(PolicyConverter.toPolicy(item));
                changed.add(saved);
            }
        }

        return changed;
    }
}
