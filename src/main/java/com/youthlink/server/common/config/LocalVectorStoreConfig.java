package com.youthlink.server.common.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("local")
public class LocalVectorStoreConfig {

    @Bean
    public VectorStore vectorStore() {
        return new VectorStore() {
            @Override
            public void add(List<Document> documents) {}

            @Override
            public void delete(List<String> idList) {}

            @Override
            public void delete(Filter.Expression filterExpression) {}

            @Override
            public List<Document> similaritySearch(SearchRequest request) {
                return List.of();
            }
        };
    }
}
