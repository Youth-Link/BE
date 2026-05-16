package com.youthlink.server.domain.notification.repository;

import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.notification.entity.Notification;
import com.youthlink.server.domain.policy.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMemberOrderByCreatedAtDesc(Member member);
    boolean existsByMemberAndPolicy(Member member, Policy policy);
}
