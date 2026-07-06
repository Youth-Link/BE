package com.youthlink.server.common.security;

import com.youthlink.server.common.security.code.AuthErrorCode;
import com.youthlink.server.common.security.exception.AuthException;
import com.youthlink.server.domain.member.code.MemberErrorCode;
import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.member.exception.MemberException;
import com.youthlink.server.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentMemberProvider {

    private final MemberRepository memberRepository;

    public Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthException(AuthErrorCode.UNAUTHORIZED);
        }

        String email = authentication.getName();
        if (email == null || email.isBlank() || "anonymousUser".equals(email)) {
            throw new AuthException(AuthErrorCode.UNAUTHORIZED);
        }

        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Long getCurrentMemberId() {
        return getCurrentMember().getId();
    }
}
