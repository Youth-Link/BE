package com.youthlink.server.domain.chat.service;

import com.youthlink.server.domain.chat.dto.ChatMessageResponse;
import com.youthlink.server.domain.chat.dto.ChatRequest;
import com.youthlink.server.domain.chat.dto.ChatResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Profile("local")
public class MockChatService implements ChatService {

    @Override
    public ChatResponse chat(Long memberId, ChatRequest request) {
        return ChatResponse.builder()
                .reply("현재 로컬 환경입니다. AI 상담 기능(Gemini/Chroma)은 다른 개발자가 연동 중이므로, 이 답변은 테스트용 메시지입니다.")
                .sessionId(request.getSessionId())
                .sources(Collections.emptyList())
                .build();
    }

    @Override
    public List<ChatMessageResponse> getMessages(Long memberId, String sessionId) {
        return Collections.emptyList();
    }

    @Override
    public void deleteSession(Long memberId, String sessionId) {
    }
}
