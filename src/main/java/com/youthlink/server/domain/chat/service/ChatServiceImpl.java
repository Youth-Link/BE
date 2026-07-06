package com.youthlink.server.domain.chat.service;

import com.youthlink.server.domain.chat.entity.ChatMessage;
import com.youthlink.server.domain.chat.repository.ChatMessageRepository;
import com.youthlink.server.domain.chat.dto.ChatMessageResponse;
import com.youthlink.server.domain.chat.dto.ChatRequest;
import com.youthlink.server.domain.chat.dto.ChatResponse;
import com.youthlink.server.domain.chat.dto.PolicySource;
import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.member.repository.MemberRepository;
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
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    private final MemberRepository memberRepository;
    private final ResourceLoader resourceLoader;

    private static final String SYSTEM_PROMPT_PATH = "classpath:prompts/system-prompt.st";
    private static final int TOP_K_RESULTS = 5;

    @Override
    @Transactional
    public ChatResponse chat(Long memberId, ChatRequest request) {
        // Vector Store에서 유사 정책 검색, 최대 5개 - TOP_K_RESULTS
        List<Document> relevantDocs = searchPolicies(request.getMessage(), memberId);

        // 컨텍스트 문자열 생성 - 텍스트+정책명 = AI가 참고하는 지식 베이스(context) 문자열 생성
        String context = buildContext(relevantDocs);

        if (relevantDocs.isEmpty()) {
            String reply = "관련 정책 정보를 찾을 수 없습니다. 지역, 나이, 취업 상태, 관심 분야를 조금 더 구체적으로 입력해 주세요.";
            saveMessages(memberId, request.getSessionId(), request.getMessage(), reply);
            return ChatResponse.builder()
                    .reply(reply)
                    .sessionId(request.getSessionId())
                    .sources(Collections.emptyList())
                    .build();
        }

        // 이전 대화 이력 조회 - 이전 대화의 흐름을 유지하기 위함.
        List<Message> conversationHistory = loadConversationHistory(memberId, request.getSessionId());

        // 메시지 구성 (시스템 + 이력 + 사용자 질문)
        List<Message> messages = new ArrayList<>();
        // SYS 메시지 - 지식 베이스(context) + 답변 규칙(SYSTEM_PROMPT)
        messages.add(new SystemMessage(loadSystemPrompt(context)));
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

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessages(Long memberId, String sessionId) {
        return chatMessageRepository.findByMemberIdAndSessionIdOrderByCreatedAtAsc(memberId, sessionId).stream()
                .map(this::toChatMessageResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteSession(Long memberId, String sessionId) {
        chatMessageRepository.deleteByMemberIdAndSessionId(memberId, sessionId);
    }

    private List<Document> searchPolicies(String query, Long memberId) {
        try {
            String region = memberRepository.findById(memberId)
                    .map(Member::getRegion)
                    .filter(r -> r != null && !r.isBlank())
                    .orElse(null);

            if (region != null) {
                List<Document> filtered = runSimilaritySearch(query, buildRegionFilter(region));
                if (!filtered.isEmpty()) {
                    return filtered;
                }
            }
            return runSimilaritySearch(query, null);
        } catch (Exception e) {
            log.warn("벡터 검색 실패: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<Document> runSimilaritySearch(String query, Filter.Expression filterExpression) {
        SearchRequest.Builder builder = SearchRequest.builder()
                .query(query)
                .topK(TOP_K_RESULTS);
        if (filterExpression != null) {
            builder.filterExpression(filterExpression);
        }
        return vectorStore.similaritySearch(builder.build());
    }

    private Filter.Expression buildRegionFilter(String region) {
        return new FilterExpressionBuilder().eq("region", region).build();
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

    private List<Message> loadConversationHistory(Long memberId, String sessionId) {
        List<ChatMessage> history = chatMessageRepository
                .findTop10ByMemberIdAndSessionIdOrderByCreatedAtDesc(memberId, sessionId);

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
                        .policyId(doc.getMetadata().getOrDefault("policyId", "").toString())
                        .policyName(doc.getMetadata().getOrDefault("policyName", "").toString())
                        .url(doc.getMetadata().getOrDefault("sourceUrl", "").toString())
                        .organization(doc.getMetadata().getOrDefault("organization", "").toString())
                        .build())
                .collect(Collectors.toList());
    }

    private String loadSystemPrompt(String context) {
        try {
            Resource resource = resourceLoader.getResource(SYSTEM_PROMPT_PATH);
            String prompt = resource.getContentAsString(StandardCharsets.UTF_8);
            return prompt.replace("{context}", context);
        } catch (IOException e) {
            log.warn("시스템 프롬프트 파일을 읽지 못했습니다: {}", e.getMessage());
            return "제공된 정책 컨텍스트를 기반으로만 답변하세요.\n\n" + context;
        }
    }

    private ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .sessionId(chatMessage.getSessionId())
                .role(chatMessage.getRole())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
