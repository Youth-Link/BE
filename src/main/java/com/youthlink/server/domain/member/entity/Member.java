package com.youthlink.server.domain.member.entity;

import com.youthlink.server.common.base.BaseTimeEntity;
import com.youthlink.server.domain.member.enums.EducationLevel;
import com.youthlink.server.domain.member.enums.Gender;
import com.youthlink.server.domain.region.entity.Region;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private EducationLevel education;

    @Column(length = 50)
    private String employmentStatus;

    @Column(length = 50)
    private String incomeLevel;

    private String profileImageUrl;

    public void updateProfile(String name, Integer age, Gender gender,
            Region region, EducationLevel education, String employmentStatus, String incomeLevel) {
        if (name != null)
            this.name = name;
        if (age != null)
            this.age = age;
        if (gender != null)
            this.gender = gender;
        if (region != null)
            this.region = region;
        if (education != null)
            this.education = education;
        if (employmentStatus != null)
            this.employmentStatus = employmentStatus;
        if (incomeLevel != null)
            this.incomeLevel = incomeLevel;
    }

    // 프로필 필수 정보가 모두 입력되었는지 확인
    // 소셜 로그인 직후에는 email, name만 존재하므로 나머지 필수값이 없으면 프로필 미완성 상태로 판단
    public boolean isProfileComplete() {
        return name != null && age != null && gender != null && region != null && education != null;
    }
}
