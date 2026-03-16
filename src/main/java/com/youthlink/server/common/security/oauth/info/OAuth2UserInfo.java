package com.youthlink.server.common.security.oauth.info;

public interface OAuth2UserInfo {
    String getProviderId(); // 소셜 서비스 식별자
    String getName(); // 사용자 이름
    String getEmail(); // 사용자 이메일
    String getProvider(); // kakao / google 같은 서비스 이름
}
