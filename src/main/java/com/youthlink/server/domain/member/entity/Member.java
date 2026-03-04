package com.youthlink.server.domain.member.entity;

import com.youthlink.server.common.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private Integer age;

    private String region;

    @Column(length = 50)
    private String education;

    @Column(length = 50)
    private String employmentStatus;

    @Column(length = 50)
    private String incomeLevel;

    private String profileImageUrl;

    @Builder
    public Member(String email, String name, Integer age, String region,
            String education, String employmentStatus, String incomeLevel) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.region = region;
        this.education = education;
        this.employmentStatus = employmentStatus;
        this.incomeLevel = incomeLevel;
    }

    public void updateProfile(String name, Integer age, String region,
            String education, String employmentStatus, String incomeLevel) {
        if (name != null)
            this.name = name;
        if (age != null)
            this.age = age;
        if (region != null)
            this.region = region;
        if (education != null)
            this.education = education;
        if (employmentStatus != null)
            this.employmentStatus = employmentStatus;
        if (incomeLevel != null)
            this.incomeLevel = incomeLevel;
    }
}
