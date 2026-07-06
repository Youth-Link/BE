package com.youthlink.server.domain.policy.service;

import com.youthlink.server.domain.policy.entity.Policy;

import java.util.List;

public interface PolicyEmbeddingService {
    /**
     * Policy 목록을 Spring AI Document로 변환하여 Chroma VectorStore에 upsert한다.
     * Document ID는 bizId로 고정하여 중복 적재를 방지한다.
     */
    void embedPolicies(List<Policy> policies);
}
