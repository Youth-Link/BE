package com.youthlink.server.domain.auth.service;

import org.springframework.stereotype.Service;

import com.youthlink.server.common.security.JwtProperties;
import com.youthlink.server.common.security.JwtTokenProvider;
import com.youthlink.server.common.security.code.AuthErrorCode;
import com.youthlink.server.common.security.exception.AuthException;
import com.youthlink.server.domain.auth.converter.AuthConverter;
import com.youthlink.server.domain.auth.dto.AuthResDto;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @Override
    public AuthResDto.ReissueDto reissue(HttpServletRequest request, HttpServletResponse response) {
        // 1. Refresh Token 추출
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        if (refreshToken == null) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 2. RefreshToken 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AuthException(AuthErrorCode.UNAUTHORIZED);
        }

        // 3. 새 토큰 생성 (RTR)
        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(email);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);

        // 4. 새 리프레시 토큰 쿠키 저장
        Cookie newCookie = new Cookie("refreshToken", newRefreshToken);
        newCookie.setHttpOnly(true);
        newCookie.setSecure(false); // 로컬 테스트용. 운영 환경에서는 true로 변경
        newCookie.setPath("/");
        newCookie.setMaxAge((int) (jwtProperties.getRefreshTokenExpiration() / 1000));
        response.addCookie(newCookie);

        return AuthConverter.toReissueDto(newAccessToken);
    }

    @Override
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 로컬 테스트용
        cookie.setPath("/");
        cookie.setMaxAge(0); // 기간 0
        response.addCookie(cookie);
    }
}
