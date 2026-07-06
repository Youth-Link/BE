package com.youthlink.server.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PolicySource {

    private String policyId;
    private String policyName;
    private String url;
    private String organization;
}
