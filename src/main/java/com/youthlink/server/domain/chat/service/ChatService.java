package com.youthlink.server.domain.chat.service;

import com.youthlink.server.domain.chat.dto.ChatRequest;
import com.youthlink.server.domain.chat.dto.ChatMessageResponse;
import com.youthlink.server.domain.chat.dto.ChatResponse;

import java.util.List;

public interface ChatService {

    ChatResponse chat(Long memberId, ChatRequest request);

    List<ChatMessageResponse> getMessages(Long memberId, String sessionId);

    void deleteSession(Long memberId, String sessionId);
}
