package com.youthlink.server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "Bearer Authentication";

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(jwtSchemeName);

        SecurityScheme securityScheme = new SecurityScheme()
                .name(jwtSchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT 토큰을 입력해주세요 (Bearer 접두사 불필요)");

        return new OpenAPI()
                .info(new Info()
                        .title("Youth-Link API")
                        .description("RAG 기반 개인화 청년 정책 추천 및 상담 서비스 API")
                        .version("v1.0.0"))
                .addSecurityItem(securityRequirement)
                .components(new Components()
                        .addSecuritySchemes(jwtSchemeName, securityScheme));
    }
}
