package com.example.search.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Search Api")
                .description("중간 지점 추천 프로젝트")
                .version("1.0.0");
    }

    @Bean
    public GroupedOpenApi searchGroup() {
        List<Tag> tags = List.of(
                new Tag().name("탐색 API").description("탐색 API")
        );

        return GroupedOpenApi.builder()
                .group("탐색 API")
                .pathsToMatch("/search/**")
                .addOpenApiCustomizer(openApi -> openApi.setTags(tags))
                .build();
    }
}
