package com.youthlink.server.domain.chat.controller;

import com.youthlink.server.domain.chat.dto.ChatRequest;
import com.youthlink.server.domain.chat.dto.ChatResponse;
import com.youthlink.server.common.apipayload.ApiResponse;
import com.youthlink.server.common.apipayload.code.GeneralSuccessCode;
import com.youthlink.server.domain.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat", description = "RAG 기반 정책 상담 채팅 API")
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Profile("!test")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "정책 상담 채팅", description = "사용자 메시지를 기반으로 관련 정책을 검색하고 AI 답변을 생성합니다")
    @PostMapping
    public ResponseEntity<ApiResponse<ChatResponse>> chat(
            @Valid @RequestBody ChatRequest request) {
        // SecurityContext에서 인증된 사용자 ID 추출 (OAuth2 연동 후 변경)
        Long memberId = 1L;
        ChatResponse response = chatService.chat(memberId, request);
        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK, response));
    }
}
