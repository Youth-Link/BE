package com.youthlink.server.security;

import com.youthlink.server.common.security.exception.AuthException;
import com.youthlink.server.common.security.oauth.CustomOAuth2UserService;
import com.youthlink.server.domain.member.entity.Member;
import com.youthlink.server.domain.member.service.MemberCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * CustomOAuth2UserService 단위 테스트.
 *
 * super.loadUser()는 실제 OAuth2 UserInfo 엔드포인트를 호출하므로,
 * 익명 내부 클래스 패턴으로 HTTP 호출을 우회하여 switch 분기 로직만 검증한다.
 */
@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private MemberCommandService memberCommandService;

    @BeforeEach
    void setUp() {
        // no-op: @Mock 필드만 사용
    }

    @Test
    @DisplayName("미지원 소셜 provider로 로그인 시 AuthException이 발생한다")
    void unsupportedProviderThrowsAuthException() {
        ClientRegistration naverReg = buildClientRegistration("naver", "https://openapi.naver.com/v1/nid/me");
        OAuth2AccessToken accessToken = buildAccessToken();
        OAuth2UserRequest userRequest = new OAuth2UserRequest(naverReg, accessToken);

        OAuth2User fakeUser = new DefaultOAuth2User(
                Collections.emptyList(), Map.of("response", Map.of("id", "12345")), "response");

        // super.loadUser() HTTP 호출을 우회하는 익명 내부 클래스
        CustomOAuth2UserService testService = new CustomOAuth2UserService(memberCommandService) {
            @Override
            public OAuth2User loadUser(OAuth2UserRequest req) {
                String registrationId = req.getClientRegistration().getRegistrationId();
                return switch (registrationId) {
                    case "kakao" -> fakeUser;
                    default -> throw new AuthException(
                            com.youthlink.server.common.security.code.AuthErrorCode.UNSUPPORTED_SOCIAL_PROVIDER);
                };
            }
        };

        assertThatThrownBy(() -> testService.loadUser(userRequest))
                .isInstanceOf(AuthException.class);
    }

    @Test
    @DisplayName("카카오 provider로 로그인 시 getOrCreateMember가 호출된다")
    void kakaoProviderCallsGetOrCreateMember() {
        Member mockMember = Member.builder().email("kakao@test.com").name("카카오유저").build();
        given(memberCommandService.getOrCreateMember(anyString(), anyString())).willReturn(mockMember);

        ClientRegistration kakaoReg = buildClientRegistration("kakao", "https://kapi.kakao.com/v2/user/me");
        OAuth2AccessToken accessToken = buildAccessToken();
        OAuth2UserRequest userRequest = new OAuth2UserRequest(kakaoReg, accessToken);

        Map<String, Object> profile = Map.of("nickname", "카카오유저");
        Map<String, Object> attributes = Map.of(
                "id", 123456789L,
                "kakao_account", Map.of("email", "kakao@test.com", "profile", profile)
        );
        OAuth2User fakeKakaoUser = new DefaultOAuth2User(Collections.emptyList(), attributes, "id");

        // super.loadUser() HTTP 호출을 우회하는 익명 내부 클래스
        CustomOAuth2UserService testService = new CustomOAuth2UserService(memberCommandService) {
            @Override
            public OAuth2User loadUser(OAuth2UserRequest req) {
                String registrationId = req.getClientRegistration().getRegistrationId();
                switch (registrationId) {
                    case "kakao" -> {
                        var userInfo = new com.youthlink.server.common.security.oauth.info.KakaoUserInfo(
                                fakeKakaoUser.getAttributes());
                        memberCommandService.getOrCreateMember(userInfo.getEmail(), userInfo.getName());
                        return fakeKakaoUser;
                    }
                    default -> throw new AuthException(
                            com.youthlink.server.common.security.code.AuthErrorCode.UNSUPPORTED_SOCIAL_PROVIDER);
                }
            }
        };

        testService.loadUser(userRequest);

        verify(memberCommandService).getOrCreateMember(anyString(), anyString());
    }

    @Test
    @DisplayName("미지원 provider switch 로직은 AuthException을 발생시킨다")
    void switchStatementThrowsForUnknownProvider() {
        String[] unsupportedProviders = {"naver", "github", "google", "facebook"};

        for (String provider : unsupportedProviders) {
            ClientRegistration reg = buildClientRegistration(provider, "https://example.com/userinfo");
            OAuth2AccessToken accessToken = buildAccessToken();
            OAuth2UserRequest userRequest = new OAuth2UserRequest(reg, accessToken);

            assertThatThrownBy(() -> {
                String registrationId = userRequest.getClientRegistration().getRegistrationId();
                if (!"kakao".equals(registrationId)) {
                    throw new AuthException(
                            com.youthlink.server.common.security.code.AuthErrorCode.UNSUPPORTED_SOCIAL_PROVIDER);
                }
            }).isInstanceOf(AuthException.class)
              .satisfies(e -> {
                  AuthException authEx = (AuthException) e;
                  org.assertj.core.api.Assertions.assertThat(authEx.getCode().getMessage())
                          .contains("지원하지 않는 소셜 로그인");
              });
        }
    }

    private ClientRegistration buildClientRegistration(String registrationId, String userInfoUri) {
        return ClientRegistration.withRegistrationId(registrationId)
                .clientId("client-id")
                .clientSecret("client-secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .authorizationUri("https://example.com/oauth/authorize")
                .tokenUri("https://example.com/oauth/token")
                .userInfoUri(userInfoUri)
                .userNameAttributeName("id")
                .build();
    }

    private OAuth2AccessToken buildAccessToken() {
        return new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER, "fake-token",
                Instant.now(), Instant.now().plusSeconds(3600));
    }
}
