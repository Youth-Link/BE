package com.youthlink.server.domain.policyalert.repository;

import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.policyalert.entity.PolicyAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyAlertRepository extends JpaRepository<PolicyAlert, Long> {
    List<PolicyAlert> findAllByMemberAndActiveTrue(Member member);
    List<PolicyAlert> findAllByActiveTrue();
}
