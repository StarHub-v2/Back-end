package com.example.starhub.config.swagger;

import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.media.MediaType;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class LoginSwaggerConfig {

    public PathItem loginPath() {
        return new PathItem()
                .post(new io.swagger.v3.oas.models.Operation()
                        .summary("로그인")
                        .description("사용자 로그인을 진행합니다.")
                        .tags(Collections.singletonList("UserController"))
                        .requestBody(new RequestBody()
                                .description("로그인 정보")
                                .content(new io.swagger.v3.oas.models.media.Content()
                                        .addMediaType("application/json", new MediaType()
                                                .schema(new ObjectSchema()
                                                        .addProperties("username", new Schema<String>().type("string").example("user123"))
                                                        .addProperties("password", new Schema<String>().type("string").example("password123"))
                                                )
                                        )
                                )
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("사용자 로그인을 성공했습니다.")
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
