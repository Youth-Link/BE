package com.youthlink.server.domain.member.service;

import com.youthlink.server.domain.member.code.MemberErrorCode;
import com.youthlink.server.domain.member.converter.MemberConverter;
import com.youthlink.server.domain.member.dto.MemberReqDto;
import com.youthlink.server.domain.member.dto.MemberResDto;
import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.member.exception.MemberException;
import com.youthlink.server.domain.member.repository.MemberRepository;
import com.youthlink.server.domain.region.entity.Region;
import com.youthlink.server.domain.region.repository.RegionRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;

    @Override
    public MemberResDto.MemberDetailDto updateProfile(Long memberId, MemberReqDto.ProfileUpdateDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // regionId가 전달되었으면 Region 엔티티를 조회하여 연결
        Region region = null;
        if (request.regionId() != null) {
            region = regionRepository.findById(request.regionId())
                    .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        }

        member.updateProfile(
                request.name(),
                request.age(),
                request.gender(),
                region,
                request.education(),
                request.employmentStatus(),
                request.incomeLevel());

        return MemberConverter.toMemberDetailDto(member);
    }

    @Override
    public MemberResDto.MemberDetailDto updateProfileByEmail(String email, MemberReqDto.ProfileUpdateDto request) {
        Member member = memberRepository.findByEmail(email)
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

    @Override
    public Member getOrCreateMember(String email, String name) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            return memberOptional.get();
        }
        return memberRepository.save(Member.builder()
                .email(email)
                .name(name)
                .build());
    }
}
