package com.youthlink.server.member;

import com.youthlink.server.member.dto.MemberResponse;
import com.youthlink.server.member.dto.ProfileUpdateRequest;

public interface MemberService {

    MemberResponse getMemberById(Long memberId);

    MemberResponse getMemberByEmail(String email);

    MemberResponse updateProfile(Long memberId, ProfileUpdateRequest request);
}
