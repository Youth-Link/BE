package com.youthlink.server.domain.policy.service;

import com.youthlink.server.domain.policy.entity.Policy;

import java.util.List;

public interface PolicyFetchService {
    /**
     * 온통청년 API를 전체 페이지 순회하여 Policy DB에 upsert한다.
     * @return 신규 저장되거나 내용이 변경된 Policy 목록
     */
    List<Policy> fetchAndSave();
}
