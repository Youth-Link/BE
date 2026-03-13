package com.youthlink.server.domain.auth.converter;

import com.youthlink.server.domain.auth.dto.AuthResDto;

import lombok.Builder;

public class AuthConverter {

    public static AuthResDto.ReissueDto toReissueDto(String accessToken) {
        return AuthResDto.ReissueDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
