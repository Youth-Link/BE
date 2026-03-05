package com.youthlink.server.domain.member.service;

import com.youthlink.server.domain.member.code.MemberErrorCode;
import com.youthlink.server.domain.member.converter.MemberConverter;
import com.youthlink.server.domain.member.dto.MemberReqDto;
import com.youthlink.server.domain.member.dto.MemberResDto;
import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.member.exception.MemberException;
import com.youthlink.server.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;

    @Override
    public MemberResDto.MemberDetailDto updateProfile(Long memberId, MemberReqDto.ProfileUpdateDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.updateProfile(
                request.name(),
                request.age(),
                request.region(),
                request.education(),
                request.employmentStatus(),
                request.incomeLevel());

        return MemberConverter.toMemberDetailDto(member);
    }
}
