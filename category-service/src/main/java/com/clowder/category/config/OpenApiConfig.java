package com.clowder.category.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI categoryServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Category Service API")
                        .description("Manages service categories for salons")
                        .version("1.0.0"));
    }
}
