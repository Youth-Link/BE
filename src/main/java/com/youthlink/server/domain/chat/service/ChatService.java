package com.youthlink.server.domain.chat.service;

import com.youthlink.server.domain.chat.dto.ChatRequest;
import com.youthlink.server.domain.chat.dto.ChatResponse;

public interface ChatService {

    ChatResponse chat(Long memberId, ChatRequest request);
}
