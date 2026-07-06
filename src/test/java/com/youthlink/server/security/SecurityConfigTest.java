package com.youthlink.server.security;

import com.youthlink.server.config.TestAiConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestAiConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Swagger UI는 인증 없이 접근 가능하다")
    void swaggerPermitAll() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("API docs는 인증 없이 접근 가능하다")
    void apiDocsPermitAll() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("토큰 재발급 API는 인증 없이 접근 가능하다 - Security가 막지 않으므로 403이 아니다")
    void reissuePermitAll() throws Exception {
        // refreshToken 쿠키 없이 요청 → 서비스 레이어 예외(4xx)가 발생하지만 Security 403(Forbidden)은 아님
        int status = mockMvc.perform(post("/api/auth/reissue"))
                .andReturn().getResponse().getStatus();
        org.assertj.core.api.Assertions.assertThat(status).isNotEqualTo(403);
    }

    @Test
    @DisplayName("인증 없이 보호 경로 접근 시 401을 반환한다")
    void protectedEndpointReturns401() throws Exception {
        mockMvc.perform(get("/api/members/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("인증 없이 정책 목록 접근 시 401을 반환한다")
    void policyEndpointRequiresAuth() throws Exception {
        mockMvc.perform(get("/api/policies"))
                .andExpect(status().isUnauthorized());
    }
}
