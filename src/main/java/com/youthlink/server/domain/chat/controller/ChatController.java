package com.youthlink.server.domain.chat.controller;

import com.youthlink.server.domain.chat.dto.ChatRequest;
import com.youthlink.server.domain.chat.dto.ChatMessageResponse;
import com.youthlink.server.domain.chat.dto.ChatResponse;
import com.youthlink.server.common.apipayload.ApiResponse;
import com.youthlink.server.common.apipayload.code.GeneralSuccessCode;
import com.youthlink.server.common.security.CurrentMemberProvider;
import com.youthlink.server.domain.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat", description = "RAG 기반 정책 상담 채팅 API")
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Profile("!test")
public class ChatController {

    private final ChatService chatService;
    private final CurrentMemberProvider currentMemberProvider;

    @Operation(summary = "정책 상담 채팅", description = "사용자 메시지를 기반으로 관련 정책을 검색하고 AI 답변을 생성합니다")
    @PostMapping
    public ResponseEntity<ApiResponse<ChatResponse>> chat(
            @Valid @RequestBody ChatRequest request) {
        Long memberId = currentMemberProvider.getCurrentMemberId();
        ChatResponse response = chatService.chat(memberId, request);
        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK, response));
    }

    @Operation(summary = "채팅 세션 이력 조회", description = "세션 ID 기준으로 저장된 대화 이력을 시간순으로 조회합니다")
    @GetMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getMessages(
            @PathVariable String sessionId) {
        Long memberId = currentMemberProvider.getCurrentMemberId();
        List<ChatMessageResponse> response = chatService.getMessages(memberId, sessionId);
        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK, response));
    }

    @Operation(summary = "채팅 세션 이력 삭제", description = "현재 로그인 사용자의 특정 세션 대화 이력을 삭제합니다")
    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<ApiResponse<String>> deleteSession(
            @PathVariable String sessionId) {
        Long memberId = currentMemberProvider.getCurrentMemberId();
        chatService.deleteSession(memberId, sessionId);
        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK, null));
    }
}
