package com.youthlink.server.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatResponse {

    private String reply;
    private String sessionId;
    private List<PolicySource> sources;
}
