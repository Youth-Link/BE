package com.youthlink.server.domain.region.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"sido", "sigungu"}))
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String sido; // 시/도 (예: "서울특별시")

    @Column(nullable = false, length = 20)
    private String sigungu; // 시/군/구 (예: "강남구")

    // 프롬프트 주입용 지역 문자열 반환.
    // 예: "서울특별시 강남구"
    public String toPromptString() {
        return sido + " " + sigungu;
    }
}
