package com.youthlink.server.domain.chat.service;

import com.youthlink.server.domain.chat.entity.ChatMessage;
import com.youthlink.server.domain.chat.repository.ChatMessageRepository;
import com.youthlink.server.domain.chat.dto.ChatRequest;
import com.youthlink.server.domain.chat.dto.ChatResponse;
import com.youthlink.server.domain.chat.dto.PolicySource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("!local")
public class ChatServiceImpl implements ChatService {

    private final ChatClient.Builder chatClientBuilder;
    private final VectorStore vectorStore;
    private final ChatMessageRepository chatMessageRepository;

    private static final String SYSTEM_PROMPT = """
            당신은 '유스링크(Youth-Link)'의 청년 정책 상담 AI 어시스턴트입니다.

            ## 역할
            - 청년 정책에 대해 친절하고 정확하게 안내합니다.
            - 제공된 정책 컨텍스트를 기반으로만 답변합니다.
            - 컨텍스트에 없는 내용은 "해당 정보를 찾을 수 없습니다"라고 안내합니다.

            ## 답변 규칙
            1. 정책명, 지원 내용, 신청 자격, 신청 방법을 구조화하여 답변합니다.
            2. 답변 마지막에 참고한 정책의 출처를 명시합니다.
            3. 일상적인 한국어로 쉽게 설명합니다.
            4. 불확실한 정보는 추측하지 않습니다.

            ## 제공된 정책 컨텍스트
            {context}
            """;

    private static final int MAX_HISTORY_SIZE = 10;
    private static final int TOP_K_RESULTS = 5;

    @Override
    @Transactional
    public ChatResponse chat(Long memberId, ChatRequest request) {
        // Vector Store에서 유사 정책 검색, 최대 5개 - TOP_K_RESULTS
        List<Document> relevantDocs = searchPolicies(request.getMessage());

        // 컨텍스트 문자열 생성 - 텍스트+정책명 = AI가 참고하는 지식 베이스(context) 문자열 생성
        String context = buildContext(relevantDocs);

        // 이전 대화 이력 조회 - 이전 대화의 흐름을 유지하기 위함.
        List<Message> conversationHistory = loadConversationHistory(request.getSessionId());

        // 메시지 구성 (시스템 + 이력 + 사용자 질문)
        List<Message> messages = new ArrayList<>();
        // SYS 메시지 - 지식 베이스(context) + 답변 규칙(SYSTEM_PROMPT)
        messages.add(new SystemMessage(SYSTEM_PROMPT.replace("{context}", context)));
        messages.addAll(conversationHistory);
        messages.add(new UserMessage(request.getMessage()));

        // Gemini LLM 호출 - 메시지를 담고 답변을 받음.
        ChatClient chatClient = chatClientBuilder.build();
        String reply = chatClient.prompt(new Prompt(messages))
                .call()
                .content();

        // 대화 이력 저장
        saveMessages(memberId, request.getSessionId(), request.getMessage(), reply);

        // 출처 정보 추출
        List<PolicySource> sources = extractSources(relevantDocs);

        return ChatResponse.builder()
                .reply(reply)
                .sessionId(request.getSessionId())
                .sources(sources)
                .build();
    }

    private List<Document> searchPolicies(String query) {
        try {
            SearchRequest searchRequest = SearchRequest.builder()
                    .query(query)
                    .topK(TOP_K_RESULTS)
                    .build();
            return vectorStore.similaritySearch(searchRequest);
        } catch (Exception e) {
            log.warn("벡터 검색 실패: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private String buildContext(List<Document> documents) {
        if (documents.isEmpty()) {
            return "관련 정책 정보를 찾을 수 없습니다.";
        }
        return documents.stream()
                .map(doc -> {
                    String policyName = doc.getMetadata().getOrDefault("policyName", "").toString();
                    String content = doc.getText();
                    return "【정책: " + policyName + "】\n" + content;
                })
                .collect(Collectors.joining("\n\n---\n\n"));
    }

    private List<Message> loadConversationHistory(String sessionId) {
        List<ChatMessage> history = chatMessageRepository
                .findTop10BySessionIdOrderByCreatedAtDesc(sessionId);

        List<Message> messages = new ArrayList<>();
        List<ChatMessage> reversed = new ArrayList<>(history);
        Collections.reverse(reversed);

        for (ChatMessage msg : reversed) {
            if (msg.getRole() == ChatMessage.Role.USER) {
                messages.add(new UserMessage(msg.getContent()));
            } else {
                messages.add(new AssistantMessage(msg.getContent()));
            }
        }
        return messages;
    }

    private void saveMessages(Long memberId, String sessionId, String userMessage, String assistantReply) {
        chatMessageRepository.save(ChatMessage.builder()
                .memberId(memberId)
                .sessionId(sessionId)
                .role(ChatMessage.Role.USER)
                .content(userMessage)
                .build());

        chatMessageRepository.save(ChatMessage.builder()
                .memberId(memberId)
                .sessionId(sessionId)
                .role(ChatMessage.Role.ASSISTANT)
                .content(assistantReply)
                .build());
    }

    private List<PolicySource> extractSources(List<Document> documents) {
        return documents.stream()
                .map(doc -> PolicySource.builder()
                        .policyId(doc.getMetadata().getOrDefault("bizId", "").toString())
                        .policyName(doc.getMetadata().getOrDefault("policyName", "").toString())
                        .url(doc.getMetadata().getOrDefault("rfcSiteUrl1", "").toString())
                        .organization(doc.getMetadata().getOrDefault("cnsgNmor", "").toString())
                        .build())
                .collect(Collectors.toList());
    }
}
