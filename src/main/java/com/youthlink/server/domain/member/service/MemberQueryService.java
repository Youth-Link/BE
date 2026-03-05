package com.youthlink.server.domain.member.service;

import com.youthlink.server.domain.member.dto.MemberResDto;

public interface MemberQueryService {
    MemberResDto.MemberDetailDto getMemberById(Long memberId);

    MemberResDto.MemberDetailDto getMemberByEmail(String email);
}
