package com.youthlink.server.domain.member.service;

import com.youthlink.server.domain.member.dto.MemberReqDto;
import com.youthlink.server.domain.member.dto.MemberResDto;

public interface MemberCommandService {
    MemberResDto.MemberDetailDto updateProfile(Long memberId, MemberReqDto.ProfileUpdateDto request);
}
