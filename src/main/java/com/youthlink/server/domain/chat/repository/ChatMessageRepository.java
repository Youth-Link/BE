package com.youthlink.server.domain.chat.repository;

import com.youthlink.server.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    List<ChatMessage> findByMemberIdAndSessionIdOrderByCreatedAtAsc(Long memberId, String sessionId);

    List<ChatMessage> findTop10BySessionIdOrderByCreatedAtDesc(String sessionId);

    List<ChatMessage> findTop10ByMemberIdAndSessionIdOrderByCreatedAtDesc(Long memberId, String sessionId);

    void deleteByMemberIdAndSessionId(Long memberId, String sessionId);
}
