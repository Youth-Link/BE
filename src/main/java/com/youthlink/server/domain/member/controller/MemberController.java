package com.youthlink.server.domain.member.controller;

import com.youthlink.server.common.apipayload.ApiResponse;
import com.youthlink.server.common.security.CurrentMemberProvider;
import com.youthlink.server.common.security.code.AuthErrorCode;
import com.youthlink.server.common.security.exception.AuthException;
import com.youthlink.server.domain.member.dto.MemberReqDto;
import com.youthlink.server.domain.member.dto.MemberResDto;
import com.youthlink.server.domain.member.service.MemberCommandService;
import com.youthlink.server.domain.member.service.MemberQueryService;
import com.youthlink.server.domain.member.code.MemberSuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 프로필 관리 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    private final CurrentMemberProvider currentMemberProvider;

    @Operation(summary = "내 프로필 조회", description = "현재 로그인한 사용자의 프로필을 조회합니다")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResDto.MemberDetailDto>> getMyProfile() {
        Long memberId = currentMemberProvider.getCurrentMemberId();
        return ResponseEntity.status(MemberSuccessCode.MEMBER_OK.getStatus())
                .body(ApiResponse.onSuccess(MemberSuccessCode.MEMBER_OK, memberQueryService.getMemberById(memberId)));
    }

    @Operation(summary = "프로필 수정", description = "현재 로그인한 사용자의 프로필을 수정합니다")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<MemberResDto.MemberDetailDto>> updateMyProfile(
            @Valid @RequestBody MemberReqDto.ProfileUpdateDto request) {
        Long memberId = currentMemberProvider.getCurrentMemberId();
        return ResponseEntity.status(MemberSuccessCode.MEMBER_UPDATE_SUCCESS.getStatus())
                .body(ApiResponse.onSuccess(MemberSuccessCode.MEMBER_UPDATE_SUCCESS,
                        memberCommandService.updateProfile(memberId, request)));
    }

    @Operation(summary = "회원 프로필 조회 (ID)", description = "회원 ID로 프로필을 조회합니다")
    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResDto.MemberDetailDto>> getMemberProfile(
            @PathVariable Long memberId) {
        Long currentMemberId = currentMemberProvider.getCurrentMemberId();
        if (!currentMemberId.equals(memberId)) {
            throw new AuthException(AuthErrorCode.FORBIDDEN);
        }
        return ResponseEntity.status(MemberSuccessCode.MEMBER_OK.getStatus())
                .body(ApiResponse.onSuccess(MemberSuccessCode.MEMBER_OK, memberQueryService.getMemberById(memberId)));
    }
}
