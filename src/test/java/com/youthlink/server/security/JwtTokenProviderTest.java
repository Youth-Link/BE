package com.youthlink.server.security;

import com.youthlink.server.common.security.JwtProperties;
import com.youthlink.server.common.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("test-secret-key-must-be-at-least-32-characters-long");
        properties.setAccessTokenExpiration(3600000L);
        properties.setRefreshTokenExpiration(1209600000L);
        properties.setOauth2RedirectUri("http://localhost:3000/oauth/callback");

        jwtTokenProvider = new JwtTokenProvider(properties);
    }

    @Test
    @DisplayName("AccessToken 생성 후 검증이 성공한다")
    void createAndValidateAccessToken() {
        String email = "test@example.com";
        String token = jwtTokenProvider.createAccessToken(email);

        assertThat(token).isNotBlank();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("RefreshToken 생성 후 검증이 성공한다")
    void createAndValidateRefreshToken() {
        String email = "test@example.com";
        String token = jwtTokenProvider.createRefreshToken(email);

        assertThat(token).isNotBlank();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("AccessToken에서 email을 정확히 추출한다")
    void extractEmailFromToken() {
        String email = "user@youthlink.com";
        String token = jwtTokenProvider.createAccessToken(email);

        String extracted = jwtTokenProvider.getEmailFromToken(token);

        assertThat(extracted).isEqualTo(email);
    }

    @Test
    @DisplayName("유효하지 않은 토큰은 검증에 실패한다")
    void invalidTokenValidationFails() {
        assertThat(jwtTokenProvider.validateToken("invalid.token.value")).isFalse();
    }

    @Test
    @DisplayName("토큰으로 Authentication 객체를 생성할 수 있다")
    void getAuthenticationFromToken() {
        String email = "test@example.com";
        String token = jwtTokenProvider.createAccessToken(email);

        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo(email);
    }
}
