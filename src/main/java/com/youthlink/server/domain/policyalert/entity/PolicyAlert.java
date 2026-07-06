package com.youthlink.server.domain.policyalert.entity;

import com.youthlink.server.common.base.BaseTimeEntity;
import com.youthlink.server.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PolicyAlert extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 50)
    private String keyword;

    @Column(length = 50)
    private String region;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    public void deactivate() {
        this.active = false;
    }
}
