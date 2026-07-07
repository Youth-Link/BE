package com.youthlink.server.domain.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EducationLevel {
    BELOW_MIDDLE_SCHOOL("중졸 이하"),
    HIGH_SCHOOL_GRADUATE("고졸"),
    ASSOCIATE_DEGREE("대졸(전문대)"),
    BACHELOR_DEGREE("대졸(4년제)"),
    MASTERS_DEGREE("석사"),
    DOCTORATE_DEGREE("박사");

    private final String description;
}
