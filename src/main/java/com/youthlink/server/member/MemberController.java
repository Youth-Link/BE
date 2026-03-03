package com.youthlink.server.member;

import com.youthlink.server.common.ApiResponse;
import com.youthlink.server.member.dto.MemberResponse;
import com.youthlink.server.member.dto.ProfileUpdateRequest;
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

    private final MemberService memberService;

    @Operation(summary = "내 프로필 조회", description = "현재 로그인한 사용자의 프로필을 조회합니다")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> getMyProfile() {
        // TODO: SecurityContext에서 인증된 사용자 ID 추출 (OAuth2 연동 후 변경)
        Long memberId = 1L;
        MemberResponse response = memberService.getMemberById(memberId);
        return ResponseEntity.ok(new ApiResponse<>(true, "프로필 조회 성공", response));
    }

    @Operation(summary = "프로필 수정", description = "현재 로그인한 사용자의 프로필을 수정합니다")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMyProfile(
            @Valid @RequestBody ProfileUpdateRequest request) {
        // TODO: SecurityContext에서 인증된 사용자 ID 추출 (OAuth2 연동 후 변경)
        Long memberId = 1L;
        MemberResponse response = memberService.updateProfile(memberId, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "프로필 수정 성공", response));
    }

    @Operation(summary = "회원 프로필 조회 (ID)", description = "회원 ID로 프로필을 조회합니다")
    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberProfile(
            @PathVariable Long memberId) {
        MemberResponse response = memberService.getMemberById(memberId);
        return ResponseEntity.ok(new ApiResponse<>(true, "프로필 조회 성공", response));
    }
}
