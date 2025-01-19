package com.example.starhub.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private final LoginSwaggerConfig loginSwaggerConfig;
    private final LogoutSwaggerConfig logoutSwaggerConfig;

    public SwaggerConfig(LoginSwaggerConfig loginSwaggerConfig, LogoutSwaggerConfig logoutSwaggerConfig) {
        this.loginSwaggerConfig = loginSwaggerConfig;
        this.logoutSwaggerConfig = logoutSwaggerConfig;
    }

    @Bean
    public OpenAPI openAPI() {
        // Access 토큰 설정
        String accessTokenKey = "access";
        SecurityRequirement accessSecurityRequirement = new SecurityRequirement().addList(accessTokenKey);
        SecurityScheme accessSecurityScheme = new SecurityScheme()
                .name(accessTokenKey)
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT");

        Components components = new Components()
                .addSecuritySchemes(accessTokenKey, accessSecurityScheme);

        return new OpenAPI()
                .components(components)
                .addSecurityItem(accessSecurityRequirement)
                .info(apiInfo())
                .path("/api/v1/login", loginSwaggerConfig.loginPath())
                .path("/api/v1/logout", logoutSwaggerConfig.logoutPath());
    }

    private Info apiInfo() {
        return new Info()
                .title("StarHub API")
                .description("StarHub API 문서")
                .version("1.0.0");
    }
}
