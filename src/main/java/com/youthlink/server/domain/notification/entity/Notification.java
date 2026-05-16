package com.youthlink.server.domain.notification.entity;

import com.youthlink.server.common.base.BaseTimeEntity;
import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.policy.entity.Policy;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private Policy policy;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String content;

    @Column(nullable = false)
    @Builder.Default
    private boolean read = false;

    public void markAsRead() {
        this.read = true;
    }
}
