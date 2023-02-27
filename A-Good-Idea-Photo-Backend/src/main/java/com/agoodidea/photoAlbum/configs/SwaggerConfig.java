package com.agoodidea.photoAlbum.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi sysUserApi() {
        String[] paths = {"/**"};
        String[] packagedToMatch = {"com.agoodidea.photoAlbum.controller"};
        return GroupedOpenApi.builder().group("ALL API")
                .pathsToMatch(paths)
                .addOperationCustomizer((operation, handlerMethod) ->
                        operation.addParametersItem(new HeaderParameter()
                                .name("token")
                                .example("token_")
                                .description("登录验证信息")
                                .schema(new StringSchema()
                                        ._default("BR").name("groupCode").description("集团code"))))
                .packagesToScan(packagedToMatch).build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info().title("A Good Idea Photo Album")
                .version("1.0")
                .description("一个好主意");
    }
}
