package com.youthlink.server.domain.policyalert.service;

import com.youthlink.server.domain.member.code.MemberErrorCode;
import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.member.exception.MemberException;
import com.youthlink.server.domain.member.repository.MemberRepository;
import com.youthlink.server.domain.policyalert.code.PolicyAlertErrorCode;
import com.youthlink.server.domain.policyalert.converter.PolicyAlertConverter;
import com.youthlink.server.domain.policyalert.dto.PolicyAlertReqDto;
import com.youthlink.server.domain.policyalert.dto.PolicyAlertResDto;
import com.youthlink.server.domain.policyalert.entity.PolicyAlert;
import com.youthlink.server.domain.policyalert.exception.PolicyAlertException;
import com.youthlink.server.domain.policyalert.repository.PolicyAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PolicyAlertServiceImpl implements PolicyAlertService {

    private final PolicyAlertRepository policyAlertRepository;
    private final MemberRepository memberRepository;

    @Override
    public PolicyAlertResDto.AlertDetailDto createAlert(String email, PolicyAlertReqDto.CreateDto request) {
        Member member = findMember(email);
        PolicyAlert alert = policyAlertRepository.save(PolicyAlertConverter.toPolicyAlert(member, request));
        return PolicyAlertConverter.toAlertDetailDto(alert);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyAlertResDto.AlertDetailDto> getMyAlerts(String email) {
        Member member = findMember(email);
        return policyAlertRepository.findAllByMemberAndActiveTrue(member).stream()
                .map(PolicyAlertConverter::toAlertDetailDto)
                .toList();
    }

    @Override
    public void deleteAlert(String email, Long alertId) {
        PolicyAlert alert = policyAlertRepository.findById(alertId)
                .orElseThrow(() -> new PolicyAlertException(PolicyAlertErrorCode.ALERT_NOT_FOUND));

        if (!alert.getMember().getEmail().equals(email)) {
            throw new PolicyAlertException(PolicyAlertErrorCode.ALERT_FORBIDDEN);
        }

        alert.deactivate();
    }

    private Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
