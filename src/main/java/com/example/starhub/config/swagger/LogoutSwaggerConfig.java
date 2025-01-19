package com.example.starhub.config.swagger;

import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class LogoutSwaggerConfig {

    public PathItem logoutPath() {
        return new PathItem()
                .post(new io.swagger.v3.oas.models.Operation()
                        .summary("로그아웃")
                        .description("사용자 로그아웃 엔드포인트")
                        .tags(Collections.singletonList("UserController"))
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("로그아웃을 진행합니다.")
                                        .content(new io.swagger.v3.oas.models.media.Content()
                                                .addMediaType("application/json", new MediaType()
                                                        .schema(new ObjectSchema()
                                                                .addProperties("status", new Schema<String>().type("string"))
                                                                .addProperties("code", new Schema<String>().type("string"))
                                                                .addProperties("message", new Schema<String>().type("string"))
                                                                .addProperties("data", new ObjectSchema()
                                                                        .addProperties("username", new Schema<String>().type("string"))
                                                                        .addProperties("isProfileComplete", new Schema<Boolean>().type("boolean"))
                                                                )
                                                        )
                                                )
                                        )
                                )

                        )
                );
    }
}
