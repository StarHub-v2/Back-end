package com.example.starhub.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

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

        OpenAPI openAPI = new OpenAPI()
                .components(components)
                .addSecurityItem(accessSecurityRequirement)
                .info(apiInfo());

        // 로그인 엔드포인트 추가
        openAPI.path("/api/v1/login", new PathItem()
                .post(new io.swagger.v3.oas.models.Operation()
                        .summary("로그인")
                        .description("사용자 로그인 엔드포인트")
                        .tags(Collections.singletonList("UserController"))
                        .requestBody(new RequestBody().description("로그인 정보")
                                .content(new io.swagger.v3.oas.models.media.Content()
                                        .addMediaType("application/json", new io.swagger.v3.oas.models.media.MediaType()
                                                .schema(new ObjectSchema()
                                                        .addProperties("username", new Schema<String>().type("string"))
                                                        .addProperties("password", new Schema<String>().type("string"))
                                                )
                                        )
                                )
                        )
                        .responses(new io.swagger.v3.oas.models.responses.ApiResponses()
                                .addApiResponse("200", new io.swagger.v3.oas.models.responses.ApiResponse().description("로그인 성공"))
                        )
                )
        );

        // 로그아웃 엔드포인트 추가
        openAPI.path("/api/v1/logout", new PathItem()
                .post(new io.swagger.v3.oas.models.Operation()
                        .summary("로그아웃")
                        .description("사용자 로그아웃 엔드포인트")
                        .tags(Collections.singletonList("UserController"))
                        .responses(new io.swagger.v3.oas.models.responses.ApiResponses()
                                .addApiResponse("200", new io.swagger.v3.oas.models.responses.ApiResponse().description("로그아웃 성공"))
                        )
                )
        );

        return openAPI;
    }

    private Info apiInfo() {
        return new Info()
                .title("StarHub API")
                .description("StarHub API 문서")
                .version("1.0.0");
    }
}
