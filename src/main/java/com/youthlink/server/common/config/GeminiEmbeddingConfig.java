package com.youthlink.server.common.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.IntStream;

@Configuration
public class GeminiEmbeddingConfig {

    @Bean
    public EmbeddingModel embeddingModel(
            @Value("${spring.ai.google.genai.embedding.api-key}") String apiKey,
            @Value("${spring.ai.google.genai.embedding.options.model}") String model) {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
        return new GeminiEmbeddingModel(restClient, apiKey, model);
    }

    static class GeminiEmbeddingModel implements EmbeddingModel {

        private final RestClient restClient;
        private final String apiKey;
        private final String model;

        GeminiEmbeddingModel(RestClient restClient, String apiKey, String model) {
            this.restClient = restClient;
            this.apiKey = apiKey;
            this.model = model;
        }

        @Override
        public float[] embed(Document document) {
            return call(new EmbeddingRequest(List.of(document.getText()), null))
                    .getResults().get(0).getOutput();
        }

        @Override
        public EmbeddingResponse call(EmbeddingRequest request) {
            List<EmbedRequest> requests = request.getInstructions().stream()
                    .map(text -> new EmbedRequest(new EmbedContent(List.of(new EmbedPart(text)))))
                    .toList();

            BatchEmbedResponse response = restClient.post()
                    .uri("/v1beta/models/{model}:batchEmbedContents?key={key}", model, apiKey)
                    .body(new BatchEmbedRequest(requests))
                    .retrieve()
                    .body(BatchEmbedResponse.class);

            List<Embedding> embeddings = IntStream.range(0, response.embeddings().size())
                    .mapToObj(i -> {
                        List<Double> vals = response.embeddings().get(i).values();
                        float[] arr = new float[vals.size()];
                        for (int j = 0; j < vals.size(); j++) arr[j] = vals.get(j).floatValue();
                        return new Embedding(arr, i);
                    })
                    .toList();

            return new EmbeddingResponse(embeddings);
        }

        record BatchEmbedRequest(List<EmbedRequest> requests) {}
        record EmbedRequest(EmbedContent content) {}
        record EmbedContent(List<EmbedPart> parts) {}
        record EmbedPart(String text) {}

        @JsonIgnoreProperties(ignoreUnknown = true)
        record BatchEmbedResponse(List<EmbedValues> embeddings) {}

        @JsonIgnoreProperties(ignoreUnknown = true)
        record EmbedValues(List<Double> values) {}
    }
}
