package com.youthlink.server.domain.member.service;

import com.youthlink.server.domain.member.dto.MemberReqDto;
import com.youthlink.server.domain.member.dto.MemberResDto;
import com.youthlink.server.domain.member.entity.Member;

public interface MemberCommandService {
    MemberResDto.MemberDetailDto updateProfile(Long memberId, MemberReqDto.ProfileUpdateDto request);

    Member getOrCreateMember(String email, String name);
}
