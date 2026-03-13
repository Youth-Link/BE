package com.youthlink.server.domain.auth.service;

import com.youthlink.server.domain.auth.dto.AuthResDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResDto.ReissueDto reissue(HttpServletRequest request, HttpServletResponse response);

    void logout(HttpServletResponse response);
}
