package com.youthlink.server.common.security.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youthlink.server.common.security.code.AuthErrorCode;
import com.youthlink.server.common.security.exception.AuthException;
import com.youthlink.server.common.security.oauth.info.KakaoUserInfo;
import com.youthlink.server.common.security.oauth.info.OAuth2UserInfo;
import com.youthlink.server.domain.member.service.MemberCommandService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberCommandService memberCommandService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. 소셜 서비스(Kakao)에서 사용자 정보를 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. 어느 소셜 서비스인지 확인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. 소셜 서비스별로 사용자 정보 매핑
        OAuth2UserInfo oAuth2UserInfo = switch (registrationId) {
            case "kakao" -> new KakaoUserInfo(oAuth2User.getAttributes());
            default -> throw new AuthException(AuthErrorCode.UNSUPPORTED_SOCIAL_PROVIDER);
        };

        // 4. DB 저장 및 업데이트 (없다면 새로 등록)
        memberCommandService.getOrCreateMember(oAuth2UserInfo.getEmail(), oAuth2UserInfo.getName());

        return oAuth2User;
    }

}
