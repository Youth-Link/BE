package com.youthlink.server.domain.auth.dto;

import lombok.Builder;

public class AuthResDto {

    @Builder
    public record ReissueDto(String accessToken) {}
}
