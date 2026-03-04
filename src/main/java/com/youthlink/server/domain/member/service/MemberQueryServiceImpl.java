package com.youthlink.server.domain.member.service;

import com.youthlink.server.domain.member.code.MemberErrorCode;
import com.youthlink.server.domain.member.converter.MemberConverter;
import com.youthlink.server.domain.member.dto.MemberResDto;
import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.member.exception.MemberException;
import com.youthlink.server.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;

    @Override
    public MemberResDto.MemberDetailDto getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return MemberConverter.toMemberDetailDto(member);
    }

    @Override
    public MemberResDto.MemberDetailDto getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return MemberConverter.toMemberDetailDto(member);
    }
}
