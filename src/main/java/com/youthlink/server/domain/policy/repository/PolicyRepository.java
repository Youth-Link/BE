package com.youthlink.server.domain.policy.repository;

import com.youthlink.server.domain.policy.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Optional<Policy> findByBizId(String bizId);
}
