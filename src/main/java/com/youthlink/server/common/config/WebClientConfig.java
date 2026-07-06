package com.youthlink.server.common.config;

import com.youthlink.server.domain.policy.config.YouthPolicyApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final YouthPolicyApiProperties youthPolicyApiProperties;

    @Bean
    public RestClient youthPolicyRestClient() {
        return RestClient.builder()
                .baseUrl(youthPolicyApiProperties.getUrl())
                .build();
    }
}
