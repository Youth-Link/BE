package com.youthlink.server.config;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 테스트 환경에서 Spring AI VectorStore/ChatModel 의존성을 충족시키기 위한 설정.
 * Chroma/GenAI 자동 구성이 비활성화된 상태에서 no-op 구현체를 제공한다.
 */
@TestConfiguration
@Profile("test")
public class TestAiConfig {

    @Bean
    public VectorStore vectorStore() {
        return new VectorStore() {
            @Override
            public void add(List<Document> documents) {
                // no-op in tests
            }

            @Override
            public void delete(List<String> idList) {
                // no-op in tests
            }

            @Override
            public void delete(Filter.Expression filterExpression) {
                // no-op in tests
            }

            @Override
            public List<Document> similaritySearch(SearchRequest request) {
                return List.of();
            }
        };
    }

    @Bean
    public ChatModel chatModel() {
        return new ChatModel() {
            @Override
            public ChatResponse call(Prompt prompt) {
                return new ChatResponse(List.of(new Generation(new AssistantMessage("test-reply"))));
            }

            @Override
            public Flux<ChatResponse> stream(Prompt prompt) {
                return Flux.just(call(prompt));
            }
        };
    }
}
