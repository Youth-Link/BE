package com.youthlink.server.domain.policy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "youth-center.api")
public class YouthPolicyApiProperties {
    private String url;
    private String key;
}
